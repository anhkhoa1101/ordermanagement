// payment/internal/PaymentListener.java
package com.ordermanagement.payment.internal;

import com.ordermanagement.order.OrderPlaced;
import com.ordermanagement.payment.PaymentCompleted;
import com.ordermanagement.payment.PaymentFailed;
import java.math.BigDecimal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class PaymentListener {
    // mô phỏng: đơn trên 100 triệu bị từ chối để thử nhánh lỗi
    private static final BigDecimal LIMIT = new BigDecimal("100000000");

    private final ApplicationEventPublisher events;

    PaymentListener(ApplicationEventPublisher events) { this.events = events; }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(OrderPlaced e) {
        if (e.amount().compareTo(LIMIT) > 0) {
            events.publishEvent(new PaymentFailed(e.orderId(), "Vượt hạn mức thanh toán"));
        } else {
            events.publishEvent(new PaymentCompleted(e.orderId()));
        }
    }
}