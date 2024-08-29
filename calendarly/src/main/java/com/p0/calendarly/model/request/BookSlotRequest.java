package com.p0.calendarly.model.request;

import com.p0.calendarly.model.User;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class BookSlotRequest {
    @NonNull
    private Long availabilityId;

    @NonNull
    private Long userId;

    @NonNull
    private String name;

    private String description;

    @NonNull
    List<User> participants;
}
