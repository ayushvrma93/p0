package com.p0.calendarly.controller;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.User;
import com.p0.calendarly.model.request.CreateUserRequest;
import com.p0.calendarly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User createUser(@RequestBody CreateUserRequest request) throws CustomException {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) throws CustomException {
        return userService.findById(id);
    }

    @GetMapping("/email")
    public User getUserByEmail(@RequestParam String email) throws CustomException {
        return userService.findByEmail(email);
    }
}
