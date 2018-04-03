package com.kapparhopi.skelestarter.backend.persistence.domain.backend;

import com.kapparhopi.skelestarter.enums.RolesEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Role implements Serializable {

    /**
     * The Serial Version UID for Serializable classes.
     */
    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();

    /**
     * Full constructor.
     *
     * @param rolesEnum
     */
    public Role(RolesEnum rolesEnum) {
        this.id = rolesEnum.getId();
        this.name = rolesEnum.getRoleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Role role = (Role) o;

        return id == role.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
