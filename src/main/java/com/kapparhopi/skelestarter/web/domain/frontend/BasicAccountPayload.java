package com.kapparhopi.skelestarter.web.domain.frontend;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Getter
@Setter
@ToString
public class BasicAccountPayload implements Serializable {

    /**
     * The Serial Version UID for Serializable classes.
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String description;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String country;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BasicAccountPayload that = (BasicAccountPayload) o;

        return username != null ? username.equals(that.username) : that.username == null;

    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

}
