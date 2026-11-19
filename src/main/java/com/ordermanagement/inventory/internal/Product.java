// inventory/internal/Product.java
package com.ordermanagement.inventory.internal;

import com.ordermanagement.shared.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(schema = "inventory", name = "products")
public class Product {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private int stock;

    protected Product() {}

    public Product(String name, BigDecimal price, int stock) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    void decrease(int qty) {
        if (stock < qty) throw new BusinessException("Không đủ tồn kho cho sản phẩm " + name);
        stock -= qty;
    }

    void increase(int qty) { stock += qty; }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
}