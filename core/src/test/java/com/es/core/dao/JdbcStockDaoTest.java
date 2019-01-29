package com.es.core.dao;

import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class JdbcStockDaoTest {
    private static final String SQL_QUERY_FOR_INSERT_PHONE = "insert into phones (id, brand, model, price) values (?, ?, ?, ?)";
    private static final String SQL_QUERY_FOR_CLEAR_PHONES = "delete from phones";
    private static final String SQL_QUERY_FOR_SETTING_STOCK = "insert into stocks (phoneId, stock, reserved) values (?,?,?)";
    private static final String SQL_QUERY_FOR_GETTING_RESERVED_QUANTITY = "select reserved from stocks where phoneId=?";
    private static final String SQL_QUERY_FOR_GETTING_STOCK_QUANTITY = "select stock from stocks where phoneId=?";
    private static final int INITIAL_PHONE_STOCK_VALUE = 5;
    private static final int INITIAL_PHONE_RESERVED_VALUE = 1;
    private static final int INITIAL_PHONE_QUANTITY = 2;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StockDao stockDao;
    private Phone initialPhone = new Phone();
    private List<OrderItem> orderItems = new ArrayList<>();

    @Before
    public void setUp() {
        initialPhone.setId(999L);
        initialPhone.setBrand("TestBrand");
        initialPhone.setModel("testModel");
        initialPhone.setPrice(BigDecimal.ONE);
        makeAndAddOrderItemToList(initialPhone, INITIAL_PHONE_QUANTITY);
        jdbcTemplate.update(SQL_QUERY_FOR_CLEAR_PHONES);
        jdbcTemplate.update(SQL_QUERY_FOR_INSERT_PHONE, initialPhone.getId(), initialPhone.getBrand(), initialPhone.getModel(), initialPhone.getPrice());
        jdbcTemplate.update(SQL_QUERY_FOR_SETTING_STOCK, initialPhone.getId(), INITIAL_PHONE_STOCK_VALUE, INITIAL_PHONE_RESERVED_VALUE);
    }

    private void makeAndAddOrderItemToList(Phone phone, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setPhone(phone);
        orderItem.setQuantity(quantity);
        orderItems.add(orderItem);
    }

    @Test
    public void shouldReturnCorrectStock() {
        int stock = stockDao.getStockFor(initialPhone.getId());

        assertEquals(INITIAL_PHONE_STOCK_VALUE, stock);
    }

    @Test
    public void shouldReserve() {
        stockDao.reserveOrderItems(orderItems);
        int actualReserved = jdbcTemplate.queryForObject(SQL_QUERY_FOR_GETTING_RESERVED_QUANTITY, Integer.class, initialPhone.getId());
        int actualStock = jdbcTemplate.queryForObject(SQL_QUERY_FOR_GETTING_STOCK_QUANTITY, Integer.class, initialPhone.getId());

        assertEquals(INITIAL_PHONE_QUANTITY+INITIAL_PHONE_RESERVED_VALUE, actualReserved);
        assertEquals(INITIAL_PHONE_STOCK_VALUE - INITIAL_PHONE_QUANTITY, actualStock);
    }
}