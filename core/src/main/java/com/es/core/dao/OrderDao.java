package com.es.core.dao;

import com.es.core.model.order.Order;

import java.util.Optional;

public interface OrderDao {
    Optional<Order> getOrder(Long id);
    void addOrder(Order order);
}
