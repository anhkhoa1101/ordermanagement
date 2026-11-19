// payment/PaymentFailed.java
package com.ordermanagement.payment;

import java.util.UUID;

public record PaymentFailed(UUID orderId, String reason) {}