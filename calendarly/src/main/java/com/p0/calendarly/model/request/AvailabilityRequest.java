package com.p0.calendarly.model.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AvailabilityRequest {
    private Long userId;
    private Timestamp startTime;
    private Timestamp endTime;
}
