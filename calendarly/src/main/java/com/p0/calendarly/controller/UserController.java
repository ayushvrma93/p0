package com.p0.calendarly.controller;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.request.CreateUserRequest;
import com.p0.calendarly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            return ResponseEntity.ok(userService.createUser(request));
        } catch (CustomException c) {
            return ResponseEntity.badRequest().body(c.getMessage()); // Return the error message as the response body
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        } catch (CustomException c){
            return ResponseEntity.badRequest().body(c.getMessage());
        }
    }

    @GetMapping("/email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) throws CustomException {
        try{
            return ResponseEntity.ok(userService.findByEmail(email));
        } catch (CustomException c){
            return ResponseEntity.badRequest().body(c.getMessage());
        }
    }
}
