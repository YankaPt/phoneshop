package com.es.core.services.order;

import com.es.core.model.order.Order;

import java.math.BigDecimal;

public interface OrderPriceService {
    BigDecimal getSubtotalOf(Order order);
    BigDecimal getDeliveryPrice();
    BigDecimal getTotalPriceOf(Order order);
}
