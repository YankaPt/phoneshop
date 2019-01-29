package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.order.Customer;
import com.es.core.services.order.OrderService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.validators.CustomerValidator;
import com.es.phoneshop.web.validators.OrderValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OrderPageControllerTest {
    private static final String ORDER_VIEW = "order";
    private static final int QUANTITY_OF_PRODUCTS = 2;
    private static final String CART_ITEMS_ATTRIBUTE = "cartItems";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String ADDRESS = "address";
    private static final String NUMBER = "number";
    private static final String REDIRECT_ORDER_OVERVIEW = "redirect:/orderOverview";
    private Cart cart = new Cart();
    private List<CartItem> cartItems = new ArrayList<>();
    private Model model = mock(Model.class);
    private Authentication authentication = mock(Authentication.class);
    private Customer customer = new Customer();
    private RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
    private OrderService orderService = mock(OrderService.class);
    private CartService cartService = mock(CartService.class);
    private PhoneService phoneService = mock(PhoneService.class);
    private TotalPriceService totalPriceService = mock(TotalPriceService.class);
    private CustomerValidator customerValidator = mock(CustomerValidator.class);
    private OrderValidator orderValidator = mock(OrderValidator.class);
    private OrderPageController controller = new OrderPageController(orderService, phoneService, cartService, totalPriceService, customerValidator, orderValidator);

    @Before
    public void setUp() {
        cart.setCartItems(cartItems);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        customer.setAddress(ADDRESS);
        customer.setContactNumber(NUMBER);
        when(cartService.getCart()).thenReturn(cart);
        when(cartService.getQuantityOfProducts()).thenReturn(QUANTITY_OF_PRODUCTS);
    }

    @Test
    public void shouldGetOrder() {
        String result = controller.getOrder(model, authentication);

        verify(model).addAttribute(CART_ITEMS_ATTRIBUTE, cartItems);
        assertEquals(ORDER_VIEW, result);
    }
}