// order/internal/OrderResponse.java
package com.ordermanagement.order.internal;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(UUID id, UUID userId, OrderStatus status, BigDecimal total, List<Line> items) {

    public record Line(UUID productId, int quantity, BigDecimal unitPrice) {}

    static OrderResponse from(Order o) {
        return new OrderResponse(
                o.getId(), o.getUserId(), o.getStatus(), o.getTotal(),
                o.getItems().stream()
                        .map(i -> new Line(i.getProductId(), i.getQuantity(), i.getUnitPrice()))
                        .toList());
    }
}