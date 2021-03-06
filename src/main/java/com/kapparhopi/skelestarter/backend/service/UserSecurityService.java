package com.kapparhopi.skelestarter.backend.service;

import com.kapparhopi.skelestarter.backend.persistence.domain.backend.User;
import com.kapparhopi.skelestarter.backend.persistence.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mdrouin
 * @since 2018-04-03
 */

@Service
@Slf4j
public class UserSecurityService implements UserDetailsService {

    /**
     * The application logger
     */

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (null == user) {
            log.warn("Username {} not found", username);
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
        return user;
    }
}
