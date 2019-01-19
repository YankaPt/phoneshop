package com.es.core.dao;

import com.es.core.model.order.OrderItem;

import java.util.List;

public interface StockDao {
    Integer getStockFor(Long key);

    void reserveOrderItems(List<OrderItem> orderItems);
}
