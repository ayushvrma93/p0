package com.p0.calendarly.utils;

import com.p0.calendarly.exceptions.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    @DisplayName("Should parse valid ISO-8601 timestamp string")
    void testParseValidTimestamp() {
        // Given
        String validTimestamp = "2023-12-25T10:30:00Z";
        
        // When
        Timestamp result = DateUtils.parse(validTimestamp);
        
        // Then
        assertNotNull(result);
        assertEquals(Timestamp.from(Instant.parse(validTimestamp)), result);
    }

    @Test
    @DisplayName("Should parse valid ISO-8601 timestamp with milliseconds")
    void testParseValidTimestampWithMilliseconds() {
        // Given
        String validTimestamp = "2023-12-25T10:30:00.123Z";
        
        // When
        Timestamp result = DateUtils.parse(validTimestamp);
        
        // Then
        assertNotNull(result);
        assertEquals(Timestamp.from(Instant.parse(validTimestamp)), result);
    }

    @Test
    @DisplayName("Should throw CustomException for invalid timestamp format")
    void testParseInvalidTimestamp() {
        // Given
        String invalidTimestamp = "2023-12-25 10:30:00";
        
        // When & Then
        CustomException exception = assertThrows(CustomException.class, 
            () -> DateUtils.parse(invalidTimestamp));
        
        assertEquals("Invalid date format", exception.getMessage());
        assertNotNull(exception.getCause());
    }

    @Test
    @DisplayName("Should throw CustomException for null input")
    void testParseNullInput() {
        // Given
        String nullTimestamp = null;
        
        // When & Then
        CustomException exception = assertThrows(CustomException.class, 
            () -> DateUtils.parse(nullTimestamp));
        
        assertEquals("Invalid date format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw CustomException for empty string")
    void testParseEmptyString() {
        // Given
        String emptyTimestamp = "";
        
        // When & Then
        CustomException exception = assertThrows(CustomException.class, 
            () -> DateUtils.parse(emptyTimestamp));
        
        assertEquals("Invalid date format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw CustomException for malformed timestamp")
    void testParseMalformedTimestamp() {
        // Given
        String malformedTimestamp = "not-a-timestamp";
        
        // When & Then
        CustomException exception = assertThrows(CustomException.class, 
            () -> DateUtils.parse(malformedTimestamp));
        
        assertEquals("Invalid date format", exception.getMessage());
        assertNotNull(exception.getCause());
    }
}
