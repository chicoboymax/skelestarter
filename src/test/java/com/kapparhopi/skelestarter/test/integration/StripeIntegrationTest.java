package com.kapparhopi.skelestarter.test.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import com.kapparhopi.skelestarter.backend.service.StripeService;
import com.kapparhopi.skelestarter.enums.PlansEnum;
import com.kapparhopi.skelestarter.utils.StripeUtils;
import com.stripe.Stripe;
import com.stripe.model.Customer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StripeIntegrationTest {

    public static final String TEST_CC_NUMBER = "4242424242424242";

    public static final int TEST_CC_EXP_MONTH = 1;

    public static final String TEST_CC_CVC_NBR = "314";

    @Autowired
    private StripeService stripeService;

    @Autowired
    private String stripeKey;

    @Before
    public void init() {
        Assert.assertNotNull(stripeKey);
        Stripe.apiKey = stripeKey;
    }

    @Test
    public void createStripeCustomer() throws Exception {

        Map<String, Object> tokenParams = new HashMap<String, Object>();
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put(StripeUtils.STRIPE_CARD_NUMBER_KEY, TEST_CC_NUMBER);
        cardParams.put(StripeUtils.STRIPE_EXPIRY_MONTH_KEY, TEST_CC_EXP_MONTH);
        cardParams.put(StripeUtils.STRIPE_EXPIRY_YEAR_KEY, LocalDate.now(Clock.systemUTC()).getYear() + 1);
        cardParams.put(StripeUtils.STRIPE_CVC_KEY, TEST_CC_CVC_NBR);
        tokenParams.put(StripeUtils.STRIPE_CARD_KEY, cardParams);

        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("description", "Customer for test@example.com");
        customerParams.put("plan", PlansEnum.PRO.getId());

        String stripeCustomerId = stripeService.createCustomer(tokenParams, customerParams);
        assertThat(stripeCustomerId, is(notNullValue()));

        Customer cu = Customer.retrieve(stripeCustomerId);
        cu.delete();

    }
}
