package com.es.phoneshop.web.services;

import com.es.core.model.cart.CartItem;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuickOrderResolver {
    public Map<Long, Integer> resolveFormData(Map<String, String> formData, ErrorsWrapper errorsWrapper) {
        Map<Long, Integer> resolvedData = new HashMap<>();
        CartItem cartItem = new CartItem();
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
