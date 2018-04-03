package com.kapparhopi.skelestarter.test.integration;

import com.kapparhopi.skelestarter.backend.persistence.domain.backend.User;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * @author mdrouin
 * @since 2018-04-03
 */
public class UserServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Rule public TestName testName = new TestName();

    @Test
    public void testCreateNewUser() throws Exception {

        User user = createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

    }

}
