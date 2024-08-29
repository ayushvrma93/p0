package com.p0.calendarly.model;

import com.p0.calendarly.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
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

    @ManyToMany
    @JoinTable(
            name = "booking_participants",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Version
    private Long version;

    private Booking(Builder builder) {
        this.availability = builder.availability;
        this.bookedBy = builder.bookedBy;
        this.status = builder.status;
        this.name = builder.name;
        this.description = builder.description;
        this.participants = builder.participants;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public Availability getAvailability() {
//        return availability;
//    }
//
//    public User getBookedBy() {
//        return bookedBy;
//    }
//
//    public BookingStatus getStatus() {
//        return status;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public List<User> getParticipants() {
//        return participants;
//    }
//
//    public Long getVersion() {
//        return version;
//    }
//
//    public void setStatus(BookingStatus status) {
//        this.status = status;
//    }

    // Static nested Builder class
    public static class Builder {
        private Availability availability;
        private User bookedBy;
        private BookingStatus status;
        private String name;
        private String description;
        private List<User> participants = new ArrayList<>();

        public Builder setAvailability(Availability availability) {
            this.availability = availability;
            return this;
        }

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

        public Builder setParticipants(List<User> participants) {
            this.participants = participants;
            return this;
        }

        public Builder addParticipant(User participant) {
            this.participants.add(participant);
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
