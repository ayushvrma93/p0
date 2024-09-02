package com.p0.calendarly.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "availabilities",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "start_time", "end_time"})})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Timestamp startTime;
    private Timestamp endTime;

    @OneToMany(mappedBy = "availability", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Booking> bookings = new ArrayList<>();

    public static class Builder {

        private User user;
        private Timestamp startTime;
        private Timestamp endTime;

        private List<Booking> bookings = new ArrayList<>();

        public Builder setUser(User user){
            this.user = user;
            return this;
        }

        public Builder setStartTime(Timestamp startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(Timestamp endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setBooking(Booking booking){
            this.bookings.add(booking);
            return this;
        }

        public Availability build() {
            Availability availability = new Availability();
            availability.setStartTime(startTime);
            availability.setEndTime(endTime);
            availability.setUser(user);
            return availability;
        }
    }
}
