// order/internal/PaymentResultListener.java
package com.ordermanagement.order.internal;

import com.ordermanagement.inventory.InventoryApi;
import com.ordermanagement.payment.PaymentCompleted;
import com.ordermanagement.payment.PaymentFailed;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class PaymentResultListener {
    private final OrderRepository orders;
    private final InventoryApi inventoryApi;

    PaymentResultListener(OrderRepository orders, InventoryApi inventoryApi) {
        this.orders = orders;
        this.inventoryApi = inventoryApi;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(PaymentCompleted e) {
        orders.findById(e.orderId()).orElseThrow().confirm();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(PaymentFailed e) {
        Order order = orders.findById(e.orderId()).orElseThrow();
        order.cancel();
        inventoryApi.release(order.stockItems());   // hoàn lại tồn kho
    }
}