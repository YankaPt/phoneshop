package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.CartItem;
import com.es.core.model.cart.CartItemWithQuantityAsString;
import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.validators.CartValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.junit.Assert.*;

public class CartPageControllerTest {
    private static final long FIRST_ITEM_PHONE_ID = 1L;
    private static final long SECOND_ITEM_PHONE_ID = 2L;
    private static final long THIRD_ITEM_PHONE_ID = 3L;
    private static final int FIRST_ITEM_QUANTITY = 1;
    private static final int SECOND_ITEM_QUANTITY = 2;
    private static final String NEGATIVE_OR_ZERO_MESSAGE = "quantity.negativeOrZero";
    private static final String NAN_MESSAGE = "quantity.NAN";
    private static final String EMPTY_MESSAGE = "field.required";
    private Model model = spy(Model.class);
    private Phone phone = new Phone();
    private Map<String, String[]> requestParams = new HashMap<>();
    private CartItem firstItem = new CartItem(FIRST_ITEM_PHONE_ID, FIRST_ITEM_QUANTITY);
    private CartItem secondItem = new CartItem(SECOND_ITEM_PHONE_ID, SECOND_ITEM_QUANTITY);
    private CartValidator validator = new CartValidator();
    private CartService cartService = mock(CartService.class);
    private PhoneService phoneService = mock(PhoneService.class);
    private TotalPriceService totalPriceService = mock(TotalPriceService.class);
    private MessageSource messageSource = mock(MessageSource.class);
    //private CartPageController controller = new CartPageController(cartService, totalPriceService, phoneService, validator, messageSource);
}