package com.es.core.dao;

import com.es.core.dao.mappers.OrderResultSetExtractor;
import com.es.core.model.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JdbcOrderDao implements OrderDao {
    private static final String SQL_STATEMENT_FOR_GETTING_ORDER = "select * from orders left join order2orderItem on orders.id = order2orderItem.orderId where orders.id = ?";
    private final JdbcTemplate jdbcTemplate;
    private PhoneDao phoneDao;
    private ResultSetExtractor<Order> orderResultSetExtractor;

    @Autowired
    public JdbcOrderDao(JdbcTemplate jdbcTemplate, PhoneDao phoneDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.phoneDao = phoneDao;
        orderResultSetExtractor = new OrderResultSetExtractor(phoneDao);
    }

    @Override
    public Optional<Order> getOrder(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(SQL_STATEMENT_FOR_GETTING_ORDER, orderResultSetExtractor, id));
    }

    @Override
    public void addOrder(Order order) {
        insertOrder(order);
        bindOrderAndOrderItems(order);
    }

    private void insertOrder(Order order) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.setTableName("orders");
        simpleJdbcInsert.execute(getOrderValues(order));
    }

    private Map<String, Object> getOrderValues(Order order) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", order.getId());
        result.put("status", order.getStatus());
        result.put("subtotal", order.getSubtotal());
        result.put("deliveryPrice", order.getDeliveryPrice());
        result.put("totalPrice", order.getTotalPrice());
        result.put("firstName", order.getFirstName());
        result.put("lastName", order.getLastName());
        result.put("deliveryAddress", order.getDeliveryAddress());
        result.put("contactPhoneNumber", order.getContactPhoneNo());
        result.put("additionalInformation", order.getAdditionalInformation());
        return result;
    }

    private void bindOrderAndOrderItems(Order order) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("order2orderItem");
        MapSqlParameterSource[] batch = order.getOrderItems().stream().map(orderItem -> new MapSqlParameterSource()
        .addValue("orderId", order.getId())
        .addValue("phoneId", orderItem.getPhone().getId())
        .addValue("quantity", orderItem.getQuantity())).toArray(MapSqlParameterSource[]::new);
        simpleJdbcInsert.executeBatch(batch);
    }

}
