package com.concrete.solution.java.test.controller;

import com.concrete.solution.java.test.model.User;
import com.concrete.solution.java.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @RequestMapping(value="/create", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User user ) {
        User u = userService.create(user);
        return new ResponseEntity<User>(u, HttpStatus.OK);
    }

    @RequestMapping(value="/login", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<User> login(@RequestBody User user) {
        user = userService.login(user.getEmail(), user.getPassword());
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        User user = userService.getById(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

}
