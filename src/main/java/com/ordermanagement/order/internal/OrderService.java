// order/internal/OrderService.java
package com.ordermanagement.order.internal;

import com.ordermanagement.inventory.InventoryApi;
import com.ordermanagement.inventory.InventoryApi.ReservedItem;
import com.ordermanagement.inventory.InventoryApi.StockItem;
import com.ordermanagement.order.OrderPlaced;
import com.ordermanagement.shared.NotFoundException;
import com.ordermanagement.user.UserApi;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class OrderService {
    private final UserApi userApi;
    private final InventoryApi inventoryApi;
    private final OrderRepository orders;
    private final ApplicationEventPublisher events;

    OrderService(UserApi userApi, InventoryApi inventoryApi,
                 OrderRepository orders, ApplicationEventPublisher events) {
        this.userApi = userApi;
        this.inventoryApi = inventoryApi;
        this.orders = orders;
        this.events = events;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest req) {
        userApi.assertActive(req.userId());

        List<StockItem> wanted = req.items().stream()
                .map(i -> new StockItem(i.productId(), i.quantity()))
                .toList();
        List<ReservedItem> reserved = inventoryApi.reserve(wanted);

        Order order = orders.save(Order.pending(req.userId(), reserved));
        events.publishEvent(new OrderPlaced(order.getId(), order.getUserId(), order.getTotal()));
        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse get(UUID id) {
        return orders.findById(id)
                .map(OrderResponse::from)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng " + id));
    }
}