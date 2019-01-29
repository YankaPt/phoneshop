package com.es.phoneshop.web.validators;

import com.es.core.dao.StockDao;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Service
public class OrderValidator implements Validator {
    private final StockDao stockDao;

    @Autowired
    public OrderValidator(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Order.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        List<OrderItem> orderItems = ((Order) target).getOrderItems();
        orderItems.forEach(orderItem -> {
            if (orderItem.getQuantity() > stockDao.getStockFor(getPhoneIdOf(orderItem))) {
                errors.reject("quantity" + getPhoneIdOf(orderItem), "field.required");
            }
        });
    }

    private long getPhoneIdOf(OrderItem orderItem) {
        return orderItem.getPhone().getId();
    }
}
