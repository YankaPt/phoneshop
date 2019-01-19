package com.es.core.services.order;

import com.es.core.dao.OrderDao;
import com.es.core.dao.StockDao;
import com.es.core.exceptions.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderItemsConverter orderItemsConverter;
    private final OrderPriceService orderPriceService;
    private final OrderDao orderDao;
    private final StockDao stockDao;
    private AtomicLong ordersCount = new AtomicLong(0);

    @Autowired
    public OrderServiceImpl(OrderItemsConverter orderItemsConverter, OrderPriceService orderPriceService, OrderDao orderDao, StockDao stockDao) {
        this.orderItemsConverter = orderItemsConverter;
        this.orderPriceService = orderPriceService;
        this.orderDao = orderDao;
        this.stockDao = stockDao;
    }

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setOrderItems(orderItemsConverter.convertCartItemsToOrderItems(cart.getCartItems(), order));
        order.setSubtotal(orderPriceService.getSubtotalOf(order));
        order.setDeliveryPrice(orderPriceService.getDeliveryPrice());
        order.setTotalPrice(orderPriceService.getTotalPriceOf(order));
        return order;
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderDao.getOrder(orderId);
    }

    @Override
    public void placeOrder(Order order) {
        order.setId(ordersCount.getAndIncrement());
        orderDao.addOrder(order);
        stockDao.reserveOrderItems(order.getOrderItems());
    }
}
