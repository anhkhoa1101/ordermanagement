// payment/PaymentCompleted.java
package com.ordermanagement.payment;

import java.util.UUID;

public record PaymentCompleted(UUID orderId) {}