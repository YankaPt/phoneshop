package com.es.core.dao;

import com.es.core.model.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    private static final String SQL_STATEMENT_FOR_INSERT_ORDER = "";
    private final JdbcTemplate jdbcTemplate;
    private List<Order> orders = new ArrayList<>(); //replace this with db

    @Autowired
    public OrderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order getOrder(Long id) {
        return orders.stream().filter(order -> order.getId().equals(id)).findFirst().get();
    }

    @Override
    public void addOrder(Order order) {
        jdbcTemplate.update(SQL_STATEMENT_FOR_INSERT_ORDER);
        orders.add(order);
    }
}
