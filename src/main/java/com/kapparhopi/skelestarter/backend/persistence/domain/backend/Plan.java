package com.kapparhopi.skelestarter.backend.persistence.domain.backend;

import com.kapparhopi.skelestarter.enums.PlansEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Plan implements Serializable {

    /**
     * The Serial Version UID for Serializable classes.
     */
    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    private String name;


    public Plan(PlansEnum plansEnum) {
        this.id = plansEnum.getId();
        this.name = plansEnum.getPlanName();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Plan plan = (Plan) o;

        return id == plan.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

}
