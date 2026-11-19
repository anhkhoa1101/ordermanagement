// order/internal/PlaceOrderRequest.java
package com.ordermanagement.order.internal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record PlaceOrderRequest(
        @NotNull UUID userId,
        @NotEmpty List<@Valid Item> items) {

    public record Item(@NotNull UUID productId, @Min(1) int quantity) {}
}