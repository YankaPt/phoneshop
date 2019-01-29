package com.es.core.services.cart;

import com.es.core.dao.PhoneDao;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TotalPriceServiceImplTest {
    private static final long PHONE_ID = 1L;
    private static final BigDecimal PHONE_PRICE = BigDecimal.ONE;
    private static final int CART_ITEM_QUANTITY = 3;
    PhoneDao phoneDao = mock(PhoneDao.class);
    private TotalPriceService totalPriceService = new TotalPriceServiceImpl(phoneDao);

    @Test
    public void shouldCalculateTotalPrice() {
        Phone phone = new Phone();
        phone.setId(PHONE_ID);
        phone.setPrice(PHONE_PRICE);
        CartItem cartItem = new CartItem(PHONE_ID, CART_ITEM_QUANTITY);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        Cart cart = new Cart();
        cart.setCartItems(cartItems);
        when(phoneDao.get(PHONE_ID)).thenReturn(Optional.of(phone));

        BigDecimal actualPrice = totalPriceService.getTotalPriceOfProducts(cart);

        assertEquals(BigDecimal.ONE.multiply(BigDecimal.valueOf(CART_ITEM_QUANTITY)), actualPrice);
    }
}