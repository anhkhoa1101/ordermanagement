// inventory/internal/InventoryService.java
package com.ordermanagement.inventory.internal;

import com.ordermanagement.inventory.InventoryApi;
import com.ordermanagement.shared.NotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class InventoryService implements InventoryApi {
    private final ProductRepository products;

    InventoryService(ProductRepository products) { this.products = products; }

    @Override
    @Transactional
    public List<ReservedItem> reserve(List<StockItem> items) {
        List<ReservedItem> result = new ArrayList<>();
        // sắp xếp theo productId để các transaction khóa theo cùng thứ tự, tránh deadlock
        for (StockItem item : items.stream().sorted(Comparator.comparing(StockItem::productId)).toList()) {
            Product p = lock(item.productId());
            p.decrease(item.quantity());
            result.add(new ReservedItem(p.getId(), item.quantity(), p.getPrice()));
        }
        return result;
    }

    @Override
    @Transactional
    public void release(List<StockItem> items) {
        for (StockItem item : items) {
            lock(item.productId()).increase(item.quantity());
        }
    }

    @Transactional
    public Product create(String name, BigDecimal price, int stock) {
        return products.save(new Product(name, price, stock));
    }

    @Transactional(readOnly = true)
    public Product get(UUID id) {
        return products.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm " + id));
    }

    private Product lock(UUID id) {
        return products.findForUpdateById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm " + id));
    }
}