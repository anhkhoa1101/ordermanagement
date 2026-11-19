// order/internal/OrderItem.java
package com.ordermanagement.order.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(schema = "ordering", name = "order_items")
public class OrderItem {
    @Id
    private UUID id;
    @Column(name = "product_id", nullable = false)
    private UUID productId;
    @Column(nullable = false)
    private int quantity;
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    protected OrderItem() {}

    OrderItem(UUID productId, int quantity, BigDecimal unitPrice) {
        this.id = UUID.randomUUID();
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public UUID getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}