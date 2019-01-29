package com.es.core.dao;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class JdbcOrderDaoTest {
    private static final String SQL_QUERY_FOR_INSERT_PHONE = "insert into phones (id, brand, model, price) values (?, ?, ?, ?)";
    private static final String SQL_QUERY_FOR_CLEAR_PHONES = "delete from phones";
    private static final String SQL_QUERY_FOR_INSERT_ORDER_ITEM = "insert into order2orderItem (orderId, phoneId, quantity) values (?, ?, ?)";
    private static final String SQL_QUERY_FOR_INSERT_ORDER = "insert into orders (id, status, subtotal, deliveryPrice, totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_FOR_GETTING_ORDER = "select * from orders where id = ?";
    private static final String SQL_QUERY_FOR_GETTING_ORDER_ITEMS = "select * from order2orderItem where orderId = ?";
    private static final String SQL_QUERY_FOR_CLEAR_ORDERS = "delete from orders";
    private static final long FIRST_ORDER_ID = 1L;
    private static final long SECOND_ORDER_ID = 2L;
    private static final OrderStatus STATUS = OrderStatus.NEW;
    private static final BigDecimal SUBTOTAL = BigDecimal.ONE;
    private static final BigDecimal DELIVERY_PRICE = BigDecimal.ZERO;
    private static final BigDecimal TOTAL_PRICE = BigDecimal.ONE;
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String DELIVERY_ADDRESS = "address";
    private static final String CONTACT_PHONE_NO = "phoneNumber";
    private static final int INITIAL_PHONE_QUANTITY = 1;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PhoneDao phoneDao = mock(PhoneDao.class);
    private OrderDao orderDao;
    private Phone initialPhone = new Phone();
    private List<OrderItem> orderItems = new ArrayList<>();

    @Before
    public void setUp() {
        orderDao = new JdbcOrderDao(jdbcTemplate, phoneDao);
        initialPhone.setId(999L);
        initialPhone.setBrand("TestBrand");
        initialPhone.setModel("testModel");
        initialPhone.setPrice(BigDecimal.ONE);
        makeAndAddOrderItemToList(initialPhone, INITIAL_PHONE_QUANTITY);
        jdbcTemplate.update(SQL_QUERY_FOR_CLEAR_PHONES);
        jdbcTemplate.update(SQL_QUERY_FOR_CLEAR_ORDERS);
        jdbcTemplate.update(SQL_QUERY_FOR_INSERT_PHONE, initialPhone.getId(), initialPhone.getBrand(), initialPhone.getModel(), initialPhone.getPrice());
        jdbcTemplate.update(SQL_QUERY_FOR_INSERT_ORDER, FIRST_ORDER_ID, STATUS.toString(), SUBTOTAL, DELIVERY_PRICE, TOTAL_PRICE, FIRST_NAME, LAST_NAME, DELIVERY_ADDRESS, CONTACT_PHONE_NO);
        jdbcTemplate.update(SQL_QUERY_FOR_INSERT_ORDER_ITEM, FIRST_ORDER_ID, initialPhone.getId(), INITIAL_PHONE_QUANTITY);
        jdbcTemplate.update(SQL_QUERY_FOR_INSERT_ORDER, SECOND_ORDER_ID, STATUS.toString(), SUBTOTAL, DELIVERY_PRICE, TOTAL_PRICE, FIRST_NAME, LAST_NAME, DELIVERY_ADDRESS, CONTACT_PHONE_NO);
        jdbcTemplate.update(SQL_QUERY_FOR_INSERT_ORDER_ITEM, SECOND_ORDER_ID, initialPhone.getId(), INITIAL_PHONE_QUANTITY);
        when(phoneDao.get(initialPhone.getId())).thenReturn(Optional.of(initialPhone));
    }

    private void makeAndAddOrderItemToList(Phone phone, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setPhone(phone);
        orderItem.setQuantity(quantity);
        orderItems.add(orderItem);
    }

    @Test
    public void shouldReturnCorrectOrder() {
        Optional<Order> order = orderDao.getOrder(FIRST_ORDER_ID);

        assertTrue(order.isPresent());
        assertEquals(FIRST_NAME, order.get().getFirstName());
        assertEquals(STATUS, order.get().getStatus());
        assertEquals(CONTACT_PHONE_NO, order.get().getContactPhoneNo());
    }

    @Test
    public void shouldAddOrder() {
        jdbcTemplate.update(SQL_QUERY_FOR_CLEAR_ORDERS);
        Order order = initializeOrder(FIRST_ORDER_ID, STATUS, orderItems, SUBTOTAL, DELIVERY_PRICE, TOTAL_PRICE, FIRST_NAME, LAST_NAME, DELIVERY_ADDRESS, CONTACT_PHONE_NO, null);

        orderDao.addOrder(order);
        Order actualOrder = jdbcTemplate.queryForObject(SQL_QUERY_FOR_GETTING_ORDER, new BeanPropertyRowMapper<>(Order.class), order.getId());
        List<OrderItem> actualOrderItemsList = jdbcTemplate.query(SQL_QUERY_FOR_GETTING_ORDER_ITEMS, new BeanPropertyRowMapper<>(OrderItem.class), order.getId());

        assertEquals(order, actualOrder);
        assertEquals(order.getFirstName(), actualOrder.getFirstName());
        assertEquals(1, actualOrderItemsList.size());
    }

    private Order initializeOrder(long orderId, OrderStatus status, List<OrderItem> orderItems, BigDecimal subtotal, BigDecimal deliveryPrice, BigDecimal totalPrice,
                                  String firstName, String lastName, String deliveryAddress, String contactPhoneNo, String additionalInfo) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(status);
        order.setSubtotal(subtotal);
        order.setDeliveryPrice(deliveryPrice);
        order.setTotalPrice(totalPrice);
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setDeliveryAddress(deliveryAddress);
        order.setContactPhoneNo(contactPhoneNo);
        order.setOrderItems(orderItems);
        order.setAdditionalInformation(additionalInfo);
        return order;
    }

    @Test
    public void shouldReturnAllOrders() {
        List<Order> orderList = orderDao.getAllOrders();

        assertEquals(2, orderList.size());
        assertEquals(FIRST_ORDER_ID, (long) orderList.get(0).getId());
        assertEquals(SECOND_ORDER_ID, (long) orderList.get(1).getId());
    }
}