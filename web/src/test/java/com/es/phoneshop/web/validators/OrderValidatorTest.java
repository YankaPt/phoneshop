package com.es.phoneshop.web.validators;

import com.es.core.dao.StockDao;
import com.es.core.model.order.Order;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class OrderValidatorTest {
    private StockDao stockDao = mock(StockDao.class);
    private OrderValidator orderValidator = new OrderValidator(stockDao);

    @Test
    public void shouldSupportOrder() {
        Order order = new Order();

        boolean isSupport = orderValidator.supports(order.getClass());

        assertTrue(isSupport);
    }

    @Test
    public void shouldNotSupportCommonObject() {
        Object object = new Object();

        boolean isSupport = orderValidator.supports(object.getClass());

        assertFalse(isSupport);
    }
}