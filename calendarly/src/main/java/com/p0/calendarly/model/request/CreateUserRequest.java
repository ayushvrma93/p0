package com.p0.calendarly.model.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateUserRequest {

    @NonNull
    private String name;

    @NonNull
    private String email;

}
