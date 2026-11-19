// order/internal/OrderRepository.java
package com.ordermanagement.order.internal;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<Order, UUID> {}