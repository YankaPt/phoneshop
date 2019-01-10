package com.es.phoneshop.web.controller;

import com.es.core.exceptions.OutOfStockException;
import com.es.core.model.cart.CartItem;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.phoneshop.web.validators.CartItemValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AjaxCartControllerTest {
    private static final long CART_ITEM_PHONE_ID = 1L;
    private static final int CART_ITEM_QUANTITY = 1;

    private CartItem cartItem = new CartItem(CART_ITEM_PHONE_ID, CART_ITEM_QUANTITY);
    private Errors errors = mock(Errors.class);
    private CartService cartService = mock(CartService.class);
    private TotalPriceService totalPriceService = mock(TotalPriceService.class);
    private CartItemValidator validator = mock(CartItemValidator.class);
    private MessageSource messageSource = mock(MessageSource.class);
    private AjaxCartController controller = new AjaxCartController(cartService, totalPriceService, validator, messageSource);

    @Test
    public void shouldAddPhone() {
        Map<String, Object> result = controller.addPhone(cartItem, errors);
        try {
            verify(cartService).addPhone(CART_ITEM_PHONE_ID, CART_ITEM_QUANTITY);
        } catch (OutOfStockException ex) {
            fail();
        }
        assertTrue(result.containsKey("message"));
    }
}