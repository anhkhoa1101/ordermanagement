// inventory/InventoryApi.java  (PUBLIC)
package com.ordermanagement.inventory;

import java.util.List;
import java.util.UUID;

public interface InventoryApi {
    void reserve(List<StockItem> items);   // ném InsufficientStockException nếu thiếu
    void release(List<StockItem> items);

    record StockItem(UUID productId, int quantity) {}
}