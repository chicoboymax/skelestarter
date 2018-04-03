package com.kapparhopi.skelestarter.exceptions;

/**
 * @author mdrouin
 * @since 2018-04-03
 */
public class StripeException extends RuntimeException {

    public StripeException(Throwable e) {
        super(e);
    }
}
