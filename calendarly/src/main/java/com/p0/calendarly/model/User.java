package com.p0.calendarly.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    private User(){}

    public User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
    }

    public static class Builder{
        private Long id;
        private String name;
        private String email;

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setEmail(String email){
            this.email = email;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}
