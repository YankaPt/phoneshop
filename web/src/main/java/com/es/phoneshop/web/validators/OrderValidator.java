package com.es.phoneshop.web.validators;

import com.es.core.dao.StockDao;
import com.es.core.model.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
        ((Order) target).getOrderItems().forEach(orderItem -> {
            if (orderItem.getQuantity() > stockDao.getStockFor(orderItem.getPhone().getId())) {
                errors.reject("quantity" + orderItem.getPhone().getId(), "field.required");
            }
        });
    }
}
