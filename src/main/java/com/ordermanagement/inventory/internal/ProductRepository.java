// inventory/internal/ProductRepository.java
package com.ordermanagement.inventory.internal;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

interface ProductRepository extends JpaRepository<Product, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findForUpdateById(UUID id);
}