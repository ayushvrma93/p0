package com.p0.calendarly.service;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.User;
import com.p0.calendarly.model.request.CreateUserRequest;
import com.p0.calendarly.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(CreateUserRequest request) throws CustomException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("User with " + request.getEmail() + " already exists");
        }

        User user = new User.Builder()
                .setEmail(request.getEmail())
                        .setName(request.getName()).build();
        return userRepository.save(user);
    }

    public User findById(Long id) throws CustomException {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User with "+ id + " not found"));
    }

    public User findByEmail(String email) throws CustomException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User with "+ email + " not found"));
    }
}
