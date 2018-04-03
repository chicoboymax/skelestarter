package com.kapparhopi.skelestarter.enums;

import lombok.Getter;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Getter
public enum RolesEnum {

    BASIC(1, "ROLE_BASIC"),
    PRO(2, "ROLE_PRO"),
    ADMIN(3, "ROLE_ADMIN");


    private final int id;

    private final String roleName;

    RolesEnum(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
}
