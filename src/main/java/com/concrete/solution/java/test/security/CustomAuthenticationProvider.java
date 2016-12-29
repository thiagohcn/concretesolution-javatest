package com.concrete.solution.java.test.security;

import com.concrete.solution.java.test.model.User;
import com.concrete.solution.java.test.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@Configurable
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = null;
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {

            user = userRepository.getByEmail(authentication.getPrincipal().toString());
        } catch (Exception e) {
            logger.error("Error loading user, not found: " + e.getMessage(), e);
        }

        return new UsernamePasswordAuthenticationToken(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
