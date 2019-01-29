package com.es.core.services.order;

import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.services.phone.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemsConverter {
    private final PhoneService phoneService;

    @Autowired
    public OrderItemsConverter(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    public List<OrderItem> convertCartItemsToOrderItems(List<CartItem> cartItems, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        cartItems.forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPhone(phoneService.get(cartItem.getPhoneId()).get());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        });
        return orderItems;
    }
}
