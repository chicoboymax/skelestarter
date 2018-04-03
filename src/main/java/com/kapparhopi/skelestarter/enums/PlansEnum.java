package com.kapparhopi.skelestarter.enums;

import lombok.Getter;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Getter
public enum PlansEnum {

    BASIC(1, "Basic"),
    PRO(2, "Pro");

    private final int id;

    private final String planName;

    PlansEnum(int id, String planName) {
        this.id = id;
        this.planName = planName;
    }

}
