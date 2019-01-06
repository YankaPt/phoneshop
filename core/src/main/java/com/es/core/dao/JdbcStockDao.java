package com.es.core.dao;

import com.es.core.model.order.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcStockDao implements StockDao {
    private static final String SQL_FOR_GETTING_STOCK_BY_PHONE_ID = "select stock from stocks where stocks.phoneId = ?";
    private static final String SQL_FOR_CHANGE_RESERVED_QUANTITY = "update stocks set reserved = reserved + ? where phoneId = ?";
    private static final String SQL_FOR_CHANGE_STOCK_QUANTITY = "update stocks set stock = stock + ? where phoneId = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcStockDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getStockFor(Long key) {
        return jdbcTemplate.queryForObject(SQL_FOR_GETTING_STOCK_BY_PHONE_ID, Integer.class, key);
    }

    @Override
    public void increaseReservationForOrderItems(List<OrderItem> orderItems) {
        List<Object[]> batchArgs = orderItems.stream()
                .map(orderItem -> new Object[]{orderItem.getQuantity(), orderItem.getPhone().getId()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(SQL_FOR_CHANGE_RESERVED_QUANTITY, batchArgs, new int[]{Types.SMALLINT, Types.BIGINT});
    }

    @Override
    public void decreaseStockForOrderItems(List<OrderItem> orderItems) {
        List<Object[]> batchArgs = orderItems.stream()
                .map(orderItem -> new Object[]{-orderItem.getQuantity(), orderItem.getPhone().getId()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(SQL_FOR_CHANGE_STOCK_QUANTITY, batchArgs, new int[]{Types.SMALLINT, Types.BIGINT});
    }
}
