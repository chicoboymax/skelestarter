package com.kapparhopi.skelestarter.backend.service;

import com.kapparhopi.skelestarter.backend.persistence.domain.backend.PasswordResetToken;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.Plan;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.User;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.UserRole;
import com.kapparhopi.skelestarter.backend.persistence.repositories.PasswordResetTokenRepository;
import com.kapparhopi.skelestarter.backend.persistence.repositories.PlanRepository;
import com.kapparhopi.skelestarter.backend.persistence.repositories.RoleRepository;
import com.kapparhopi.skelestarter.backend.persistence.repositories.UserRepository;
import com.kapparhopi.skelestarter.enums.PlansEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Service
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles) {

        User localUser = userRepository.findByEmail(user.getEmail());

        if (localUser != null) {
            log.info("User with username {} and email {} already exist. Nothing will be done. ",
                    user.getUsername(), user.getEmail());
        } else {

            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            Plan plan = new Plan(plansEnum);
            // It makes sure the plans exist in the database
            if (!planRepository.existsById(plansEnum.getId())) {
                plan = planRepository.save(plan);
            }

            user.setPlan(plan);



            user.getUserRoles().addAll(userRoles);

            localUser = userRepository.save(user);

        }

        return localUser;

    }

    /**
     * Returns a user by username or null if a user could not be found.
     *
     * @param username The username to be found
     * @return A user by username or null if a user could not be found.
     */
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Returns a user for the given email or null if a user could not be found.
     *
     * @param email The email associated to the user to find.
     * @return a user for the given email or null if a user could not be found.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void updateUserPassword(long userId, String password) {
        password = passwordEncoder.encode(password);
        userRepository.updateUserPassword(userId, password);
        log.debug("Password updated successfully for user id {} ", userId);

        Set<PasswordResetToken> resetTokens = passwordResetTokenRepository.findAllByUserId(userId);
        if (!resetTokens.isEmpty()) {
            passwordResetTokenRepository.deleteAll(resetTokens);
        }
    }
}
