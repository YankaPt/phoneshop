package com.es.core.services.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderPriceServiceImplTest {
    private static final BigDecimal DELIVERY_PRICE = BigDecimal.ONE;
    private static final Integer ORDER_ITEM_QUANTITY = 2;
    private static final BigDecimal PHONE_PRICE = BigDecimal.ONE;
    private Phone orderItemPhone = new Phone();
    private Order order = new Order();
    private OrderItem orderItem = new OrderItem();
    private List<OrderItem> orderItems = new ArrayList<>();
    private OrderPriceService orderPriceService = new OrderPriceServiceImpl(DELIVERY_PRICE);

    @Before
    public void setUp() {
        orderItemPhone.setPrice(PHONE_PRICE);
        orderItem.setPhone(orderItemPhone);
        orderItem.setQuantity(ORDER_ITEM_QUANTITY);
        order.setOrderItems(orderItems);
        order.getOrderItems().add(orderItem);
    }

    @Test
    public void shouldReturnDeliveryPrice() {
        BigDecimal actualDeliveryPrice = orderPriceService.getDeliveryPrice();

        assertEquals(DELIVERY_PRICE, actualDeliveryPrice);
    }

    @Test
    public void shouldReturnSubtotal() {
        BigDecimal actualSubtotal = orderPriceService.getSubtotalOf(order);

        assertEquals(PHONE_PRICE.multiply(BigDecimal.valueOf(ORDER_ITEM_QUANTITY)), actualSubtotal);
    }
}