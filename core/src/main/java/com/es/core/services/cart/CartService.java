package com.es.core.services.cart;

import com.es.core.exceptions.OutOfStockException;
import com.es.core.model.cart.Cart;

import java.util.Map;

public interface CartService {
    Cart getCart();

    void addPhone(Long phoneId, Integer quantity) throws OutOfStockException;

    void update(Map<Long, Integer> items);

    void remove(Long phoneId);

    Integer getQuantityOfProducts();
}
