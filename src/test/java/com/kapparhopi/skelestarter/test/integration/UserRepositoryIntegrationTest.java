package com.kapparhopi.skelestarter.test.integration;

import com.kapparhopi.skelestarter.backend.persistence.domain.backend.Plan;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.Role;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.User;
import com.kapparhopi.skelestarter.backend.persistence.domain.backend.UserRole;
import com.kapparhopi.skelestarter.enums.PlansEnum;
import com.kapparhopi.skelestarter.enums.RolesEnum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @author mdrouin
 * @since 2018-04-03
 */
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Rule public TestName testName = new TestName();

    @Before
    public void init() {
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() throws Exception {
        Plan basicPlan = createPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);
        Optional<Plan> retrievedPlan = planRepository.findById(PlansEnum.BASIC.getId());
        Assert.assertNotNull(retrievedPlan);
    }

    @Test
    public void testCreateNewRole() throws Exception {

        Role userRole = createRole(RolesEnum.BASIC);
        roleRepository.save(userRole);

        Optional<Role> retrievedRole = roleRepository.findById(RolesEnum.BASIC.getId());
        Assert.assertNotNull(retrievedRole);
    }

    @Test
    public void createNewUser() throws Exception {

        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@devopsbuddy.com";

        User basicUser = createUser(username, email);

        Optional<User> newlyCreatedUser = userRepository.findById(basicUser.getId());
        Assert.assertNotNull(newlyCreatedUser);
        Assert.assertTrue(newlyCreatedUser.get().getId() != 0);
        Assert.assertNotNull(newlyCreatedUser.get().getPlan());
        Assert.assertNotNull(newlyCreatedUser.get().getPlan().getId());
        Set<UserRole> newlyCreatedUserUserRoles = newlyCreatedUser.get().getUserRoles();
        for (UserRole ur : newlyCreatedUserUserRoles) {
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        }

    }

    @Test
    public void testDeleteUser() {

        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@skelestarter.com";

        User basicUser = createUser(username, email);
        userRepository.deleteById(basicUser.getId());
    }

    @Test
    public void testGetUserByEmail() {
        User user = createUser(testName);

        User newlyFoundUser = userRepository.findByEmail(user.getEmail());
        Assert.assertNotNull(newlyFoundUser);
        Assert.assertNotNull(newlyFoundUser.getId());
    }

    @Test
    public void testUpdateUserPassword() throws Exception {
        Optional<User> user = Optional.ofNullable(createUser(testName));
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.get().getId());

        String newPassword = UUID.randomUUID().toString();

        userRepository.updateUserPassword(user.get().getId(), newPassword);

        user = userRepository.findById(user.get().getId());
        Assert.assertEquals(newPassword, user.get().getPassword());

    }
}
