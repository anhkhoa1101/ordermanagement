// inventory/InventoryApi.java  (PUBLIC)
package com.ordermanagement.inventory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface InventoryApi {
    /** Giữ hàng và trả về giá tại thời điểm đặt. Ném BusinessException nếu thiếu tồn kho, NotFoundException nếu không có sản phẩm. */
    List<ReservedItem> reserve(List<StockItem> items);

    /** Hoàn lại tồn kho (dùng khi thanh toán thất bại). */
    void release(List<StockItem> items);

    record StockItem(UUID productId, int quantity) {}

    record ReservedItem(UUID productId, int quantity, BigDecimal unitPrice) {}
<<<<<<< HEAD
}
=======
}
>>>>>>> 70e4663 (fix code)
