package com.es.phoneshop.web.services;

import com.es.core.model.cart.CartItem;
import com.es.phoneshop.web.validators.QuickOrderFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import java.util.*;

@Service
public class QuickOrderResolver {
    private final QuickOrderFormValidator quickOrderFormValidator;

    @Autowired
    public QuickOrderResolver(QuickOrderFormValidator quickOrderFormValidator) {
        this.quickOrderFormValidator = quickOrderFormValidator;
    }

    public Map<Long, Integer> resolveFormData(Map<String, String> formData, ErrorsWrapper errorsWrapper) {
        Map<Long, Integer> resolvedData = new HashMap<>();
        CartItem cartItem = new CartItem();
        DataBinder dataBinder = new DataBinder(formData);
        dataBinder.setValidator(quickOrderFormValidator);
        dataBinder.validate();
        errorsWrapper.setErrors(dataBinder.getBindingResult());
        formData.forEach((key, value) -> {
            if (key.startsWith("code") && value != null) {
                cartItem.setPhoneId(Long.parseLong(value));
            } else if (key.startsWith("quantity")  && value != null) {
                cartItem.setQuantity(Integer.parseInt(value));
                resolvedData.put(cartItem.getPhoneId(), cartItem.getQuantity());
            }
        });
        return resolvedData;
    }
}
