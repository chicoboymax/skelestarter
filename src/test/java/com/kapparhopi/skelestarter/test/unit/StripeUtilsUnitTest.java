package com.kapparhopi.skelestarter.test.unit;

import com.kapparhopi.skelestarter.test.integration.StripeIntegrationTest;
import com.kapparhopi.skelestarter.utils.StripeUtils;
import com.kapparhopi.skelestarter.web.domain.frontend.ProAccountPayload;

import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author mdrouin
 * @since 2018-04-03
 */
public class StripeUtilsUnitTest {

    @Test
    public void createStripeTokenParamsFromUserPayload() {

        ProAccountPayload payload = new ProAccountPayload();
        String cardNumber = StripeIntegrationTest.TEST_CC_NUMBER;
        payload.setCardNumber(cardNumber);
        String cardCode = StripeIntegrationTest.TEST_CC_CVC_NBR;
        payload.setCardCode(cardCode);
        String cardMonth = String.valueOf(StripeIntegrationTest.TEST_CC_EXP_MONTH);
        payload.setCardMonth(cardMonth);
        String cardYear = String.valueOf(LocalDate.now(Clock.systemUTC()).getYear() + 1);
        payload.setCardYear(cardYear);

        Map<String, Object> tokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);
        Map<String, Object> cardParams = (Map<String, Object>) tokenParams.get(StripeUtils.STRIPE_CARD_KEY);
        assertThat(cardNumber, is(cardParams.get(StripeUtils.STRIPE_CARD_NUMBER_KEY)));
        assertThat(cardMonth, is(String.valueOf(cardParams.get(StripeUtils.STRIPE_EXPIRY_MONTH_KEY))));
        assertThat(cardYear, is(String.valueOf(cardParams.get(StripeUtils.STRIPE_EXPIRY_YEAR_KEY))));
        assertThat(cardCode, is(cardParams.get(StripeUtils.STRIPE_CVC_KEY)));
    }
}
