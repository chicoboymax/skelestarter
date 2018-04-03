package com.kapparhopi.skelestarter.web.domain.frontend;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Getter
@Setter
public class ProAccountPayload extends BasicAccountPayload {

    private String cardNumber;
    private String cardCode;
    private String cardMonth;
    private String cardYear;
}
