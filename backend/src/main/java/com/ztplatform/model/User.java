package com.ztplatform.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = UserStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Role {
        ADMIN,
        STUDENT,
        VERIFIER;

        @JsonCreator
        public static Role fromString(String value) {
            if (value == null) return null;
            return Role.valueOf(value.toUpperCase());
        }

        @JsonValue
        public String toValue() {
            return this.name();
        }
    }

    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED;

        @JsonCreator
        public static UserStatus fromString(String value) {
            if (value == null) return null;
            return UserStatus.valueOf(value.toUpperCase());
        }

        @JsonValue
        public String toValue() {
            return this.name();
        }
    }
}