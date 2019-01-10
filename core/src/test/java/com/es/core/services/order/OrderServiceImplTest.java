package com.es.core.services.order;

import com.es.core.dao.OrderDao;
import com.es.core.dao.StockDao;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {
    private static final long FIRST_PHONE_ID = 1L;
    private static final long SECOND_PHONE_ID = 2L;
    private static final int FIRST_ITEM_QUANTITY = 1;
    private static final int SECOND_ITEM_QUANTITY = 2;
    private static final long TEST_ORDER_ID = 1L;
    private static final BigDecimal ORDER_TOTAL_PRICE = BigDecimal.ONE;
    private static final Order NEW_ORDER = new Order();
    private Phone firstPhone = new Phone();
    private Phone secondPhone = new Phone();
    private List<CartItem> cartItems = new ArrayList<>();
    private List<OrderItem> orderItems = new ArrayList<>();
    private Order expectedOrder = new Order();
    private OrderItemsConverter orderItemsConverter = mock(OrderItemsConverter.class);
    private OrderPriceService orderPriceService = mock(OrderPriceService.class);
    private OrderDao orderDao = mock(OrderDao.class);
    private StockDao stockDao = mock(StockDao.class);
    private OrderServiceImpl orderService = new OrderServiceImpl(orderItemsConverter, orderPriceService, orderDao, stockDao);
    private Cart cart = new Cart();

    @Before
    public void setUp() {
        expectedOrder.setId(TEST_ORDER_ID);
        firstPhone.setId(FIRST_PHONE_ID);
        secondPhone.setId(SECOND_PHONE_ID);
        cartItems.clear();
        cartItems.add(new CartItem(FIRST_PHONE_ID, FIRST_ITEM_QUANTITY));
        cartItems.add(new CartItem(SECOND_PHONE_ID, SECOND_ITEM_QUANTITY));
        cart.setCartItems(cartItems);
        when(orderDao.getOrder(TEST_ORDER_ID)).thenReturn(expectedOrder);
        when(orderItemsConverter.convertCartItemsToOrderItems(cartItems, expectedOrder)).thenReturn(orderItems);
        when(orderPriceService.getTotalPriceOf(NEW_ORDER)).thenReturn(ORDER_TOTAL_PRICE);
    }

    private OrderItem createOrderItem(Order order, Phone phone, Integer quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setPhone(phone);
        orderItem.setQuantity(quantity);
        return orderItem;
    }

    @Test
    public void shouldReturnCorrectOrder() {
        Order actualOrder = orderService.getOrder(TEST_ORDER_ID);

        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void shouldCreateNewOrder() {
        Order actualOrder = orderService.createOrder(cart);

        assertEquals(orderItems, actualOrder.getOrderItems());
        assertEquals(ORDER_TOTAL_PRICE, actualOrder.getTotalPrice());
    }

    @Test
    public void shouldPlaceOrder() {
        expectedOrder.setId(null);
        orderService.placeOrder(expectedOrder);

        verify(orderDao).addOrder(expectedOrder);
        assertNotNull(expectedOrder.getId());
    }
}