package com.es.phoneshop.web.controller.pages;

import com.es.core.model.order.Order;
import com.es.core.services.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderOverviewPageControllerTest {
    private static final String ORDER_OVERVIEW_ADDRESS = "orderOverview";
    private static final String REDIRECT_404_PAGE = "redirect:/404page";
    private static final String ORDER_ID_ATTRIBUTE = "orderId";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String ADDRESS = "address";
    private static final String NUMBER = "number";
    private static final long ORDER_ID = 1L;
    private Order order = new Order();
    private Model model = mock(Model.class);
    private Map<String, Object> modelAsMap = new HashMap<>();
    private OrderService orderService = mock(OrderService.class);
    private OrderOverviewPageController controller = new OrderOverviewPageController(orderService);

    @Before
    public void setUp() {
        modelAsMap.clear();
        modelAsMap.put(ORDER_ID_ATTRIBUTE, ORDER_ID);
        order.setFirstName(FIRST_NAME);
        order.setLastName(LAST_NAME);
        order.setDeliveryAddress(ADDRESS);
        order.setContactPhoneNo(NUMBER);
        when(model.asMap()).thenReturn(modelAsMap);
        when(orderService.getOrder(ORDER_ID)).thenReturn(order);
    }

    @Test
    public void shouldNotReturnOrderOverview() {
        String result = controller.getOrderOverview(model);

        assertEquals(REDIRECT_404_PAGE, result);
    }

    @Test
    public void shouldReturnOrderOverview() {
        when(model.containsAttribute(ORDER_ID_ATTRIBUTE)).thenReturn(true);

        String result = controller.getOrderOverview(model);

        assertEquals(ORDER_OVERVIEW_ADDRESS, result);
    }
}