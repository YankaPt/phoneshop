package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.cart.CartItemWithQuantityAsString;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.services.CartFormResolver;
import com.es.phoneshop.web.services.CartItemsConverter;
import com.es.phoneshop.web.services.ErrorLocalizer;
import com.es.phoneshop.web.services.ErrorsWrapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CartPageControllerTest {
    private static final String CART_PAGE_VIEW = "cart";
    private static final String EMPTY_CART_PAGE_VIEW = "emptyCart";
    private static final String REDIRECTING_ADDRESS = "redirect:/cart";
    private static final String LOCALE_ATTRIBUTE = "locale";
    private static final String OLD_CART_ITEMS_ATTRIBUTE = "oldCartItems";
    private static final long FIRST_ITEM_PHONE_ID = 1L;
    private static final long SECOND_ITEM_PHONE_ID = 2L;
    private static final int FIRST_ITEM_QUANTITY = 1;
    private static final int SECOND_ITEM_QUANTITY = 2;
    private static final Locale TEST_LOCALE = Locale.ENGLISH;

    private Map<String, String> formData = new HashMap<>();
    private Map<Long, Integer> cartItems = new HashMap<>();
    private CartItem firstItem = new CartItem(FIRST_ITEM_PHONE_ID, FIRST_ITEM_QUANTITY);
    private CartItem secondItem = new CartItem(SECOND_ITEM_PHONE_ID, SECOND_ITEM_QUANTITY);
    private List<CartItemWithQuantityAsString> cartItemsWithStringQuantity = new ArrayList<>();
    private Model model = spy(Model.class);
    private Authentication authentication = mock(Authentication.class);
    private RedirectAttributes redirectAttributes = spy(RedirectAttributes.class);
    private CartService cartService = mock(CartService.class);
    private PhoneService phoneService = mock(PhoneService.class);
    private TotalPriceService totalPriceService = mock(TotalPriceService.class);
    private CartFormResolver cartFormResolver = mock(CartFormResolver.class);
    private ErrorLocalizer errorLocalizer = mock(ErrorLocalizer.class);
    private CartItemsConverter cartItemsConverter = mock(CartItemsConverter.class);
    private CartPageController controller = new CartPageController(cartService, totalPriceService, phoneService, cartFormResolver, errorLocalizer, cartItemsConverter);

    @Before
    public void setUp() {
        model.asMap().clear();
        formData.clear();
        formData.put("_method", "PUT");
        formData.put(firstItem.getPhoneId().toString(), firstItem.getQuantity().toString());
        formData.put(secondItem.getPhoneId().toString(), secondItem.getQuantity().toString());
        when(cartItemsConverter.convertCartItemsMapToList(formData)).thenReturn(cartItemsWithStringQuantity);
        when(cartFormResolver.resolveFormData(formData, mock(ErrorsWrapper.class))).thenReturn(cartItems);
        when(cartService.getCart()).thenReturn(new Cart());
    }

    @Test
    public void shouldDeleteItem() {
        String actualReturnedValue = controller.deleteItem(FIRST_ITEM_PHONE_ID);

        verify(cartService).remove(FIRST_ITEM_PHONE_ID);
        assertEquals(REDIRECTING_ADDRESS, actualReturnedValue);
    }

    @Test
    public void shouldUpdateCart() {
        String actualReturnedValue = controller.updateCart(formData, redirectAttributes, TEST_LOCALE);

        verify(redirectAttributes).addFlashAttribute(LOCALE_ATTRIBUTE, TEST_LOCALE);
        verify(redirectAttributes).addFlashAttribute(OLD_CART_ITEMS_ATTRIBUTE, cartItemsWithStringQuantity);
        verify(cartService).update(cartItems);
        assertEquals(REDIRECTING_ADDRESS, actualReturnedValue);
    }

    @Test
    public void shouldReturnEmptyCart() {
        String actualReturnedValue = controller.getCart(model, authentication);

        assertEquals(EMPTY_CART_PAGE_VIEW, actualReturnedValue);
    }
}