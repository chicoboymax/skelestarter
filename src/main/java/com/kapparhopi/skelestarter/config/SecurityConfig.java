package com.kapparhopi.skelestarter.config;

import com.kapparhopi.skelestarter.backend.service.UserSecurityService;
import com.kapparhopi.skelestarter.web.controllers.ForgotMyPasswordController;
import com.kapparhopi.skelestarter.web.controllers.SignupController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * @author mdrouin
 * @since 2018-03-30
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * The encryption SALT.
     */
    private static final String SALT = "fdalkjalk;3jlwf00sfaof";
    /**
     * Public URLs.
     */
    private static final String[] PUBLIC_MATCHERS = {
            "/webjars/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/",
            "/about/**",
            "/contact/**",
            "/error/**/*",
            "/console/**",
            ForgotMyPasswordController.FORGOT_PASSWORD_URL_MAPPING,
            ForgotMyPasswordController.CHANGE_PASSWORD_PATH,
            SignupController.SIGNUP_URL_MAPPING
    };
    @Autowired
    private UserSecurityService userSecurityService;
    @Autowired
    private Environment env;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains("dev")) {
            http.csrf().disable();
            http.headers().frameOptions().disable();
        }

        http
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/payload")
                .failureUrl("/login?error").permitAll()
                .and()
                .logout().permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder());
    }
}
