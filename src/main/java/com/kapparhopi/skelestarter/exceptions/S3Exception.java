package com.kapparhopi.skelestarter.exceptions;

/**
 * @author mdrouin
 * @since 2018-04-03
 */
public class S3Exception extends RuntimeException {

    public S3Exception(Throwable e) {
        super(e);
    }

    public S3Exception(String s) {
        super(s);
    }
}
