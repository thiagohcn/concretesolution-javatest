package com.concrete.solution.java.test.service;

import com.concrete.solution.java.test.exception.UserCreateException;
import com.concrete.solution.java.test.exception.UserNotAuthorizedException;
import com.concrete.solution.java.test.repository.UserRepository;
import com.concrete.solution.java.test.exception.UserNotFoundException;
import com.concrete.solution.java.test.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;


    private BCryptPasswordEncoder passwordEncoder;

    public User create(User user) {
        User u = userRepository.getByEmail(user.getEmail());
        if(u != null) throw new UserCreateException();

        passwordEncoder = new BCryptPasswordEncoder();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated(new Date());
        user.setLastLogin(new Date());
        user = userRepository.saveAndFlush(user);

        final String token = jwtTokenService.generateToken(user);
        user.setToken(token);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.getByEmail(email);

        if(user == null) throw new UserNotFoundException();

        passwordEncoder = new BCryptPasswordEncoder();

        if(!passwordEncoder.matches(password, user.getPassword())) { throw new UserNotAuthorizedException();}

        user.setLastLogin(new Date());

        return userRepository.save(user);

    }

    public User getById(Long id) {
        return userRepository.findOne(id);
    }



}
