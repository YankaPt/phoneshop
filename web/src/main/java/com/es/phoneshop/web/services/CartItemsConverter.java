package com.es.phoneshop.web.services;

import com.es.core.model.cart.CartItemWithQuantityAsString;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartItemsConverter {
    public List<CartItemWithQuantityAsString> convertCartItemsMapToList(Map<String, String> map) {
        List<CartItemWithQuantityAsString> list = new ArrayList<>();
        map.forEach((key, value) -> {
            if (key.matches("\\d*") && value!=null) {
                list.add(new CartItemWithQuantityAsString(Long.parseLong(key), value));
            }
        });
        return list;
    }
}
