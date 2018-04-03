package com.kapparhopi.skelestarter.test.integration;

import com.kapparhopi.skelestarter.backend.persistence.domain.backend.Role;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.User;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.UserRole;
import com.kapparhopi.skelestarter.backend.service.UserService;
import com.kapparhopi.skelestarter.enums.PlansEnum;
import com.kapparhopi.skelestarter.enums.RolesEnum;
import com.kapparhopi.skelestarter.utils.UserUtils;

import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mdrouin
 * @since 2018-04-03
 */
public abstract class AbstractServiceIntegrationTest {

    @Autowired
    protected UserService userService;

    protected User createUser(TestName testName) {
        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@devopsbuddy.com";

        Set<UserRole> userRoles = new HashSet<>();
        User basicUser = UserUtils.createBasicUser(username, email);
        userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));

        return userService.createUser(basicUser, PlansEnum.BASIC, userRoles);
    }
}
