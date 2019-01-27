package com.es.phoneshop.web.validators;

import com.es.core.model.cart.CartItem;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CartItemValidatorTest {
    private CartItem cartItem;
    private Errors errors = spy(Errors.class);
    private CartItemValidator validator = new CartItemValidator();

    @Before
    public void setUp() {
        cartItem  = new CartItem(1000L, 10);
    }

    @Test
    public void shouldSupportCartItemClass() {
        assertTrue(validator.supports(cartItem.getClass()));
    }

    @Test
    public void shouldNotSupportNotCartItemClass() {
        assertFalse(validator.supports(Object.class));
    }

    @Test
    public void shouldNotValidateItemWithZeroQuantity() {
        cartItem.setQuantity(0);
        validator.validate(cartItem, errors);
        verify(errors).rejectValue("quantity", "quantity.negativeOrZero", "Quantity must be a positive number");
    }
}