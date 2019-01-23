package com.es.core.dao.mappers;

import com.es.core.dao.PhoneDao;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OrderListResultSetExtractor implements ResultSetExtractor {
    private BeanPropertyRowMapper<Order> orderPropertyRowMapper = new BeanPropertyRowMapper<>(Order.class);
    private PhoneDao phoneDao;

    public OrderListResultSetExtractor(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    @Override
    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            long orderId = rs.getLong("orderId");
            Order currentOrder = orders.stream()
                    .filter(order -> order.getId().equals(orderId))
                    .findFirst()
                    .orElseGet(() -> {
                        try {
                            Order order = orderPropertyRowMapper.mapRow(rs, 0);
                            order.setOrderItems(new ArrayList<>());
                            orders.add(order);
                            return order;
                        } catch (SQLException exception) {
                            return null;
                        }
                    });

            OrderItem orderItem = new OrderItem();
            orderItem.setPhone(phoneDao.get(rs.getLong("phoneId")).get());
            orderItem.setQuantity(rs.getInt("quantity"));
            orderItem.setOrder(currentOrder);
            currentOrder.getOrderItems().add(orderItem);
        }
        return orders;
    }
}
