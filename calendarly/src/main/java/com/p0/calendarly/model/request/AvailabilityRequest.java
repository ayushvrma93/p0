package com.p0.calendarly.model.request;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
public class AvailabilityRequest {
    @NonNull
    private Timestamp startTime;

    @NonNull
    private Timestamp endTime;
}
