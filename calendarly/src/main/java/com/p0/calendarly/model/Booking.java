package com.p0.calendarly.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.p0.calendarly.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "bookings")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "availability_id")
    private Availability availability;

    @ManyToOne
    @JoinColumn(name = "booked_by_user_id", nullable = false)
    private User bookedBy;

    private String name;
    private String description;

    @ManyToOne
    @JoinTable(
            name = "booking_participants",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private User otherParticipant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Version
    private Long version;

    private Timestamp startTime;

    private Timestamp endTime;

    private Booking(){}

    private Booking(Builder builder) {
        this.bookedBy = builder.bookedBy;
        this.status = builder.status;
        this.name = builder.name;
        this.description = builder.description;
        this.otherParticipant = builder.otherParticipant;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Static nested Builder class
    public static class Builder {
        private User bookedBy;
        private BookingStatus status;
        private String name;
        private String description;
        private User otherParticipant;

        private Timestamp startTime;

        private Timestamp endTime;

//        public Builder setAvailability(Availability availability) {
//            this.availability = availability;
//            return this;
//        }

        public Builder setBookedBy(User bookedBy) {
            this.bookedBy = bookedBy;
            return this;
        }

        public Builder setStatus(BookingStatus status) {
            this.status = status;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setOtherParticipant(User participant) {
            this.otherParticipant = participant;
            return this;
        }

        public Builder addParticipant(User participant) {
            this.otherParticipant = participant;
            return this;
        }

        public Builder setStartTime(Timestamp startTime){
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(Timestamp endTime){
            this.endTime = endTime;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
