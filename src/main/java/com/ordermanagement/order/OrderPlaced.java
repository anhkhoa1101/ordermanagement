// order/OrderPlaced.java
package com.ordermanagement.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderPlaced(UUID orderId, UUID userId, BigDecimal amount) {}