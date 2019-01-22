package com.es.core.services.cart;

import com.es.core.model.cart.Cart;

import java.math.BigDecimal;

public interface TotalPriceService {
    BigDecimal getTotalPriceOfProducts(Cart cart);
}
