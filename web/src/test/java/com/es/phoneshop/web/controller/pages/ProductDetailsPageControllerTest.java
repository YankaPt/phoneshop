package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProductDetailsPageControllerTest {
    private static final long PHONE_ID = 1L;
    private static final String PHONE_ATTRIBUTE = "phone";
    private Model model = spy(Model.class);
    private Phone phone = new Phone();
    private CartService cartService = mock(CartService.class);
    private PhoneService phoneService = mock(PhoneService.class);
    private TotalPriceService totalPriceService = mock(TotalPriceService.class);
    private ProductDetailsPageController controller = new ProductDetailsPageController(phoneService, cartService, totalPriceService);

    @Before
    public void setUp() {
        phone.setId(PHONE_ID);
        when(phoneService.get(PHONE_ID)).thenReturn(Optional.of(phone));
    }

    @Test
    public void shouldReturnProduct(){

        controller.showProduct(PHONE_ID, model);

        verify(model).addAttribute(PHONE_ATTRIBUTE, phone);
    }
}