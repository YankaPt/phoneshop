package com.es.core.services.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderPriceServiceImpl implements OrderPriceService {
    @Override
    public BigDecimal getSubtotalOf(Order order) {
        BigDecimal subtotalPrice = BigDecimal.ZERO;
        for (OrderItem orderItem : order.getOrderItems()) {
            subtotalPrice = subtotalPrice.add(orderItem.getPhone().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        return subtotalPrice;
    }

    @Override
    public BigDecimal getDeliveryPrice() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalPriceOf(Order order) {
        return getSubtotalOf(order).add(getDeliveryPrice());
    }
}
