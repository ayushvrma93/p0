package com.p0.calendarly.utils;

import com.p0.calendarly.exceptions.CustomException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeParseException;

public class DateUtils {

    public static Timestamp parse(String timeStampString){
        Timestamp t;
        try{
            t = Timestamp.from(Instant.parse(timeStampString));
        } catch (DateTimeParseException e) {
            throw new CustomException("Invalid date format", e);
        }
        return t;
    }
}
