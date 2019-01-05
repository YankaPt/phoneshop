package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProductDetailsPageControllerTest {
    private static final long PHONE_ID = 1L;
    private static final String PHONE_ATTRIBUTE = "phone";
    private static final String TOTAL_PRICE_ATTRIBUTE = "cartItemsPrice";
    private static final String QUANTITY_OF_PRODUCTS_ATTRIBUTE = "cartItemsAmount";
    private static final String REDIRECTING_TO_404_ADDRESS = "redirect:/404page";
    private static final String VIEW_NAME = "productDetails";
    private static final BigDecimal TOTAL_PRICE = BigDecimal.ONE;
    private static final int QUANTITY_OF_PRODUCTS = 1;

    private Model model = spy(Model.class);
    private Phone phone = new Phone();
    private CartService cartService = mock(CartService.class);
    private PhoneService phoneService = mock(PhoneService.class);
    private TotalPriceService totalPriceService = mock(TotalPriceService.class);
    private ProductDetailsPageController controller = new ProductDetailsPageController(phoneService, cartService, totalPriceService);

    @Before
    public void setUp() {
        model.asMap().clear();
        phone.setId(PHONE_ID);
        when(phoneService.get(PHONE_ID)).thenReturn(Optional.of(phone));
        when(totalPriceService.getTotalPriceOfProducts()).thenReturn(TOTAL_PRICE);
        when(cartService.getQuantityOfProducts()).thenReturn(QUANTITY_OF_PRODUCTS);
    }

    @Test
    public void shouldReturnProduct(){
        String actualResult = controller.showProduct(PHONE_ID, model);

        verify(model).addAttribute(PHONE_ATTRIBUTE, phone);
        verify(model).addAttribute(QUANTITY_OF_PRODUCTS_ATTRIBUTE, QUANTITY_OF_PRODUCTS);
        verify(model).addAttribute(TOTAL_PRICE_ATTRIBUTE, TOTAL_PRICE);
        assertEquals(VIEW_NAME, actualResult);
    }

    @Test
    public void shouldRedirectTo404Page() {
        String actualResult = controller.showProduct(null, model);

        assertEquals(REDIRECTING_TO_404_ADDRESS, actualResult);
    }
}