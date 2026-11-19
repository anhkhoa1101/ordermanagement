// inventory/internal/ProductController.java
package com.ordermanagement.inventory.internal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    public record CreateProductRequest(
            @NotBlank String name,
            @NotNull @DecimalMin("0.0") BigDecimal price,
            @Min(0) int stock) {}

    public record ProductResponse(UUID id, String name, BigDecimal price, int stock) {
        static ProductResponse from(Product p) {
            return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getStock());
        }
    }

    private final InventoryService service;

    ProductController(InventoryService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody CreateProductRequest r) {
        return ProductResponse.from(service.create(r.name(), r.price(), r.stock()));
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable UUID id) {
        return ProductResponse.from(service.get(id));
    }
}