// user/internal/UserRepository.java
package com.ordermanagement.user.internal;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
}