package com.kapparhopi.skelestarter.backend.service;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.Token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Service
@Slf4j
public class StripeService {

    @Autowired
    private String stripeKey;

    /**
     * Creates a Stripe customer and returns the Stripe customer id
     *
     * @param tokenParams    The credit card details to obtain a token. These will never be stored in the DB
     * @param customerParams The parameters which identify the customer
     * @return The stripe customer id which can then be used to perform billing operations at a later stage
     * @throws com.kapparhopi.skelestarter.exceptions If an error occurred while interacting with Stripe
     */
    public String createCustomer(Map<String, Object> tokenParams, Map<String, Object> customerParams) {

        Stripe.apiKey = stripeKey;

        String stripeCustomerId = null;
        try {
            Token token = Token.create(tokenParams);
            customerParams.put("source", token.getId());
            Customer customer = Customer.create(customerParams);
            stripeCustomerId = customer.getId();
        } catch (AuthenticationException e) {
            log.error("An authentication exception occurred while creating the Stripe customer", e);
            throw new com.kapparhopi.skelestarter.exceptions.StripeException(e);
        } catch (InvalidRequestException e) {
            log.error("An invalid request exception occurred while creating the Stripe customer", e);
            throw new com.kapparhopi.skelestarter.exceptions.StripeException(e);
        } catch (APIConnectionException e) {
            log.error("An API connection exception occurred while creating the Stripe customer", e);
            throw new com.kapparhopi.skelestarter.exceptions.StripeException(e);
        } catch (CardException e) {
            log.error("A Credit Card exception occurred while creating the Stripe customer", e);
            throw new com.kapparhopi.skelestarter.exceptions.StripeException(e);
        } catch (APIException e) {
            log.error("An API exception occurred while creating the Stripe customer", e);
            throw new com.kapparhopi.skelestarter.exceptions.StripeException(e);
        }
        return stripeCustomerId;
    }
}
