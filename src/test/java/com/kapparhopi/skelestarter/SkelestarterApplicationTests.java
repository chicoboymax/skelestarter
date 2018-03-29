package com.kapparhopi.skelestarter;

import com.kapparhopi.skelestarter.web.i18n.I18NService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SkelestarterApplicationTests {

    @Autowired
    private I18NService i18NService;

    @Test
    public void testMessageByLocaleService() throws Exception {
        String expectedResult = "Skelestarter";
        String messageId = "index.main.callout";
        String actual = i18NService.getMessage(messageId);
        Assert.assertEquals("The actual and expected Strings don't match", expectedResult, actual);
    }
}
