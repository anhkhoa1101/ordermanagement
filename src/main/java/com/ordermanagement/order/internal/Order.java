// order/internal/Order.java
package com.ordermanagement.order.internal;

import com.ordermanagement.inventory.InventoryApi.ReservedItem;
import com.ordermanagement.inventory.InventoryApi.StockItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(schema = "ordering", name = "orders")
public class Order {
    @Id
    private UUID id;
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @Column(nullable = false)
    private BigDecimal total;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderItem> items = new ArrayList<>();

    protected Order() {}

    static Order pending(UUID userId, List<ReservedItem> reserved) {
        Order o = new Order();
        o.id = UUID.randomUUID();
        o.userId = userId;
        o.status = OrderStatus.PENDING;
        o.createdAt = Instant.now();
        o.total = BigDecimal.ZERO;
        for (ReservedItem r : reserved) {
            o.items.add(new OrderItem(r.productId(), r.quantity(), r.unitPrice()));
            o.total = o.total.add(r.unitPrice().multiply(BigDecimal.valueOf(r.quantity())));
        }
        return o;
    }

    void confirm() { this.status = OrderStatus.CONFIRMED; }
    void cancel() { this.status = OrderStatus.CANCELLED; }

    List<StockItem> stockItems() {
        return items.stream().map(i -> new StockItem(i.getProductId(), i.getQuantity())).toList();
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotal() { return total; }
    public List<OrderItem> getItems() { return items; }
}