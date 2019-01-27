package com.es.core.services.order;

import com.es.core.dao.OrderDao;
import com.es.core.dao.StockDao;
import com.es.core.exceptions.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderItemsConverter orderItemsConverter;
    private final OrderPriceService orderPriceService;
    private final OrderDao orderDao;
    private final StockDao stockDao;
    private PlatformTransactionManager transactionManager;
    private AtomicLong ordersCount = new AtomicLong(0);

    @Autowired
    public OrderServiceImpl(OrderItemsConverter orderItemsConverter, OrderPriceService orderPriceService, OrderDao orderDao, StockDao stockDao, PlatformTransactionManager transactionManager) {
        this.orderItemsConverter = orderItemsConverter;
        this.orderPriceService = orderPriceService;
        this.orderDao = orderDao;
        this.stockDao = stockDao;
        this.transactionManager = transactionManager;
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
        return orderDao.getOrder(orderId).get();
    }

    @Override
    public void placeOrder(Order order) throws OutOfStockException{
        order.setId(ordersCount.getAndIncrement());
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            stockDao.reserveOrderItems(order.getOrderItems());
            orderDao.addOrder(order);
        }
        catch (DataAccessException exception) {
            transactionManager.rollback(status);
            throw new OutOfStockException();
        }
        transactionManager.commit(status);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    @Override
    public void updateOrderStatus(Order order) {
        orderDao.updateOrderStatus(order);
        OrderStatus status = order.getStatus();
        if (status.equals(OrderStatus.DELIVERED)) {
            stockDao.removeReservationForOrderItems(order.getOrderItems());
        } else if (status.equals(OrderStatus.REJECTED)) {
            stockDao.unrollReservationForOrderItems(order.getOrderItems());
        }
    }
}
