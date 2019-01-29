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

public class OrderResultSetExtractor implements ResultSetExtractor {
    private BeanPropertyRowMapper<Order> orderPropertyRowMapper = new BeanPropertyRowMapper<>(Order.class);
    private PhoneDao phoneDao;

    public OrderResultSetExtractor(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    @Override
    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        rs.next();
        Order order = orderPropertyRowMapper.mapRow(rs, 0);
        order.setOrderItems(new ArrayList<>());
        do {
            OrderItem orderItem = new OrderItem();
            orderItem.setPhone(phoneDao.get(rs.getLong("phoneId")).get());
            orderItem.setQuantity(rs.getInt("quantity"));
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        } while (rs.next());
        return order;
    }
}
