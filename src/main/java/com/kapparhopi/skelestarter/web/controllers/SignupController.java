package com.kapparhopi.skelestarter.web.controllers;

import com.kapparhopi.skelestarter.backend.persistence.domain.backend.Plan;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.Role;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.User;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.UserRole;
import com.kapparhopi.skelestarter.backend.service.PlanService;
import com.kapparhopi.skelestarter.backend.service.S3Service;
import com.kapparhopi.skelestarter.backend.service.StripeService;
import com.kapparhopi.skelestarter.backend.service.UserService;
import com.kapparhopi.skelestarter.enums.PlansEnum;
import com.kapparhopi.skelestarter.enums.RolesEnum;
import com.kapparhopi.skelestarter.exceptions.S3Exception;
import com.kapparhopi.skelestarter.utils.StripeUtils;
import com.kapparhopi.skelestarter.utils.UserUtils;
import com.kapparhopi.skelestarter.web.domain.frontend.BasicAccountPayload;
import com.kapparhopi.skelestarter.web.domain.frontend.ProAccountPayload;
import com.stripe.exception.StripeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Controller
@Slf4j
public class SignupController {

    public static final String SIGNUP_URL_MAPPING = "/signup";
    public static final String PAYLOAD_MODEL_KEY_NAME = "payload";
    public static final String SUBSCRIPTION_VIEW_NAME = "registration/signup";
    public static final String DUPLICATED_USERNAME_KEY = "duplicatedUsername";
    public static final String DUPLICATED_EMAIL_KEY = "duplicatedEmail";
    public static final String SIGNED_UP_MESSAGE_KEY = "signedUp";
    public static final String ERROR_MESSAGE_KEY = "message";
    public static final String GENERIC_ERROR_VIEW_NAME = "error/genericError";
    /**
     * The application logger
     */

    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private StripeService stripeService;

    @GetMapping(value = SIGNUP_URL_MAPPING)
    public String signupGet(@RequestParam("planId") int planId, ModelMap model) {

        if (planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()) {
            throw new IllegalArgumentException("Plan id is not valid");
        }
        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());

        return SUBSCRIPTION_VIEW_NAME;
    }

    @PostMapping(value = SIGNUP_URL_MAPPING)
    public String signUpPost(@RequestParam(name = "planId", required = true) int planId,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @ModelAttribute(PAYLOAD_MODEL_KEY_NAME) @Valid ProAccountPayload payload,
            ModelMap model) throws IOException {

        if (planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()) {
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(ERROR_MESSAGE_KEY, "Plan id does not exist");
            return SUBSCRIPTION_VIEW_NAME;
        }

        this.checkForDuplicates(payload, model);

        boolean duplicates = false;

        List<String> errorMessages = new ArrayList<>();

        if (model.containsKey(DUPLICATED_USERNAME_KEY)) {
            log.warn("The username already exists. Displaying error to the user");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            errorMessages.add("Username already exist");
            duplicates = true;
        }

        if (model.containsKey(DUPLICATED_EMAIL_KEY)) {
            log.warn("The email already exists. Displaying error to the user");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            errorMessages.add("Email already exist");
            duplicates = true;
        }

        if (duplicates) {
            model.addAttribute(ERROR_MESSAGE_KEY, errorMessages);
            return SUBSCRIPTION_VIEW_NAME;
        }

        // There are certain info that the user doesn't set, such as profile image URL, Stripe customer id,
        // plans and roles
        log.debug("Transforming user payload into User domain object");
        User user = UserUtils.fromWebUserToDomainUser(payload);

        // Stores the profile image on Amazon S3 and stores the URL in the user's record
        if (file != null && !file.isEmpty()) {

            String profileImageUrl = s3Service.storeProfileImage(file, payload.getUsername());
            if (profileImageUrl != null) {
                user.setProfileImageUrl(profileImageUrl);
            } else {
                log.warn("There was a problem uploading the profile image to S3. The user's profile will" +
                        " be created without the image");
            }

        }

        // Sets the Plan and the Roles (depending on the chosen plan)
        log.debug("Retrieving plan from the database");
        Optional<Plan> selectedPlan = planService.findPlanById(planId);
        if (!selectedPlan.isPresent()) {
            log.error("The plan id {} could not be found. Throwing exception.", planId);
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(ERROR_MESSAGE_KEY, "Plan id not found");
            return SUBSCRIPTION_VIEW_NAME;
        }
        user.setPlan(selectedPlan.get());

        User registeredUser = null;

        // By default users get the BASIC ROLE
        Set<UserRole> roles = new HashSet<>();
        if (planId == PlansEnum.BASIC.getId()) {
            roles.add(new UserRole(user, new Role(RolesEnum.BASIC)));
            registeredUser = userService.createUser(user, PlansEnum.BASIC, roles);
        } else {
            roles.add(new UserRole(user, new Role(RolesEnum.PRO)));

            // Extra precaution in case the POST method is invoked programmatically
            if (StringUtils.isEmpty(payload.getCardCode()) ||
                    StringUtils.isEmpty(payload.getCardNumber()) ||
                    StringUtils.isEmpty(payload.getCardMonth()) ||
                    StringUtils.isEmpty(payload.getCardYear())) {
                log.error("One or more credit card fields is null or empty. Returning error to the user");
                model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
                model.addAttribute(ERROR_MESSAGE_KEY, "One of more credit card details is null or empty.");
                return SUBSCRIPTION_VIEW_NAME;

            }

            // If the user has selected the pro account, creates the Stripe customer to store the stripe customer id in
            // the db
            Map<String, Object> stripeTokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);

            Map<String, Object> customerParams = new HashMap<String, Object>();
            customerParams.put("description", "DevOps Buddy customer. Username: " + payload.getUsername());
            customerParams.put("email", payload.getEmail());
            customerParams.put("plan", selectedPlan.get().getId());
            log.info("Subscribing the customer to plan {}", selectedPlan.get().getName());
            String stripeCustomerId = stripeService.createCustomer(stripeTokenParams, customerParams);
            log.info("Username: {} has been subscribed to Stripe", payload.getUsername());

            user.setStripeCustomerId(stripeCustomerId);

            registeredUser = userService.createUser(user, PlansEnum.PRO, roles);
            log.debug(payload.toString());
        }

        // Auto logins the registered user
        Authentication auth = new UsernamePasswordAuthenticationToken(
                registeredUser, null, registeredUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        log.info("User created successfully");

        model.addAttribute(SIGNED_UP_MESSAGE_KEY, "true");

        return SUBSCRIPTION_VIEW_NAME;
    }

    @ExceptionHandler({StripeException.class, S3Exception.class})
    public ModelAndView signupException(HttpServletRequest request, Exception exception) {

        log.error("Request {} raised exception {}", request.getRequestURL(), exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("timestamp", LocalDate.now(Clock.systemUTC()));
        mav.setViewName(GENERIC_ERROR_VIEW_NAME);
        return mav;
    }

    //--------------> Private methods

    /**
     * Checks if the username/email are duplicates and sets error flags in the model.
     * Side effect: the method might set attributes on Model
     **/
    private void checkForDuplicates(BasicAccountPayload payload, ModelMap model) {

        // Username
        if (userService.findByUserName(payload.getUsername()) != null) {
            model.addAttribute(DUPLICATED_USERNAME_KEY, true);
        }
        if (userService.findByEmail(payload.getEmail()) != null) {
            model.addAttribute(DUPLICATED_EMAIL_KEY, true);
        }

    }
}
