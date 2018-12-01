package com.es.phoneshop.web.services;

import com.es.core.model.cart.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Service
public class CartItemValidator implements Validator {
   /* private Properties properties = new Properties();
    {
        try {
            properties.load(new FileReader("/home/yan/ExpertSoft/phoneshop/web/src/main/webapp/resources/messages.properties"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }*/

    @Override
    public boolean supports(Class<?> clazz) {
        return CartItem.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", "field.required", "Field can't be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phoneId", "field.required");
        if (errors.hasErrors()) {
            return;
        }
        CartItem cartItem = (CartItem) target;
        if (cartItem.getQuantity() < 1) {
            errors.rejectValue("quantity", "quantity.negativeOrZero", "Quantity must be a positive number");
        }
    }
}
