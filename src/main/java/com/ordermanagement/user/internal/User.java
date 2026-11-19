// user/internal/User.java
package com.ordermanagement.user.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(schema = "users", name = "users")
public class User {
    @Id
    private UUID id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(nullable = false)
    private boolean active;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected User() {}

    public User(String email, String fullName) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.fullName = fullName;
        this.active = true;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public boolean isActive() { return active; }
}