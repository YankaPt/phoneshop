package com.es.phoneshop.web.services;

import com.es.core.model.cart.CartItem;
import com.es.core.model.cart.CartItemWithQuantityAsString;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CartItemsConverterTest {
    private static final long FIRST_ITEM_PHONE_ID = 1L;
    private static final long SECOND_ITEM_PHONE_ID = 2L;
    private static final int FIRST_ITEM_QUANTITY = 1;
    private static final int SECOND_ITEM_QUANTITY = 2;
    private Map<String, String> formData = new HashMap<>();
    private CartItem firstItem = new CartItem(FIRST_ITEM_PHONE_ID, FIRST_ITEM_QUANTITY);
    private CartItem secondItem = new CartItem(SECOND_ITEM_PHONE_ID, SECOND_ITEM_QUANTITY);
    private CartItemsConverter cartItemsConverter = new CartItemsConverter();

    @Before
    public void setUp() {
        formData.clear();
        formData.put("_method", "PUT");
        formData.put(firstItem.getPhoneId().toString(), firstItem.getQuantity().toString());
        formData.put(secondItem.getPhoneId().toString(), secondItem.getQuantity().toString());
    }

    @Test
    public void shouldConvertCartItemsMapToList() {
        List<CartItemWithQuantityAsString> cartItems = cartItemsConverter.convertCartItemsMapToList(formData);

        assertEquals(2, cartItems.size());
        assertEquals(firstItem.getPhoneId(), cartItems.get(0).getPhoneId());
        assertEquals(secondItem.getQuantity().toString(), cartItems.get(1).getQuantity());
    }

}