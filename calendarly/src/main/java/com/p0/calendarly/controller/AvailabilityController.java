package com.p0.calendarly.controller;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.Availability;
import com.p0.calendarly.model.User;
import com.p0.calendarly.service.AvailabilityService;
import com.p0.calendarly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public Availability createAvailability(@RequestParam Long userId,
                                           @RequestParam Timestamp startTime,
                                           @RequestParam Timestamp endTime) throws CustomException {
        User user = userService.findById(userId);
        return availabilityService.createAvailability(user, startTime, endTime);
    }

    @GetMapping("/user/{userId}")
    public List<Availability> getUserAvailability(@PathVariable Long userId,
                                                     @RequestParam Timestamp start,
                                                     @RequestParam Timestamp end) {
        return availabilityService.getUserAvailability(userId, start, end);
    }
}

