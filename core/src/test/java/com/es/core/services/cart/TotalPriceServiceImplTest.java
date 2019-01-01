package com.es.core.services.cart;

import com.es.core.dao.PhoneDao;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TotalPriceServiceImplTest {
    private static final int FIRST_PHONE_QUANTITY = 6;
    private static final int SECOND_PHONE_QUANTITY = 2;
    private static final BigDecimal FIRST_PHONE_PRICE = new BigDecimal(5);
    private static final BigDecimal SECOND_PHONE_PRICE = new BigDecimal(3);
    private static final long FIRST_PHONE_ID = 34L;
    private static final long SECOND_PHONE_ID = 45L;
    private Cart cart = new Cart();
    private PhoneDao phoneDao = mock(PhoneDao.class);
    private TotalPriceService totalPriceService = new TotalPriceServiceImpl(cart, phoneDao);

    @Before
    public void setUp() {
        cart.getCartItems().clear();
        Phone firstPhone = initPhone(FIRST_PHONE_ID, FIRST_PHONE_PRICE);
        cart.getCartItems().add(new CartItem(firstPhone.getId(), FIRST_PHONE_QUANTITY));
        Phone secondPhone = initPhone(SECOND_PHONE_ID, SECOND_PHONE_PRICE);
        cart.getCartItems().add(new CartItem(secondPhone.getId(), SECOND_PHONE_QUANTITY));
        when(phoneDao.get(FIRST_PHONE_ID)).thenReturn(Optional.of(firstPhone));
        when(phoneDao.get(SECOND_PHONE_ID)).thenReturn(Optional.of(secondPhone));
    }

    private Phone initPhone(long phoneId, BigDecimal phonePrice) {
        Phone phone = new Phone();
        phone.setId(phoneId);
        phone.setPrice(phonePrice);
        return phone;
    }

    @Test
    public void shouldReturnCorrectTotalPrice() {
        BigDecimal expectedPrice = FIRST_PHONE_PRICE.multiply(BigDecimal.valueOf(FIRST_PHONE_QUANTITY))
                .add(SECOND_PHONE_PRICE.multiply(BigDecimal.valueOf(SECOND_PHONE_QUANTITY)));

        BigDecimal actualPrice = totalPriceService.getTotalPriceOfProducts();

        assertEquals(expectedPrice, actualPrice);
    }
}