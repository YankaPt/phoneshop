package com.es.core.services.cart;

import com.es.core.dao.PhoneDao;
import com.es.core.exceptions.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class CartServiceImplTest {
    private static final String SQL_QUERY_FOR_SETTING_STOCK = "insert into stocks (phoneId, stock, reserved) values (?,?,?)";
    private static final String SQL_QUERY_FOR_CLEAR_PHONES = "delete from phones";
    private static final Long INITIAL_CART_ITEM_PHONE_ID = 42L;
    private static final Integer INITIAL_STOCK = 10;
    private static final Integer INITIAL_RESERVED = 3;
    private static final Integer SECOND_STOCK=10;
    private static final Integer SECOND_RESERVED = 5;
    private static final Long SECOND_CART_ITEM_PHONE_ID = 10L;
    private static final String PHONE_BRAND = "brand";
    private static final String INITIAL_PHONE_MODEL = "model";
    private static final String SECOND_PHONE_MODEL = "anotherModel";

    private CartItem initialCartItem = new CartItem();
    private Phone initialPhone = new Phone();
    private Phone secondPhone = new Phone();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PhoneDao phoneDao;
    @Autowired
    private Cart cart;
    @Autowired
    private CartService cartService;

    @Before
    public void clear() {
        jdbcTemplate.update(SQL_QUERY_FOR_CLEAR_PHONES);
        setPhoneParameters(initialPhone, INITIAL_CART_ITEM_PHONE_ID, INITIAL_PHONE_MODEL);
        setPhoneParameters(secondPhone, SECOND_CART_ITEM_PHONE_ID, SECOND_PHONE_MODEL);
        jdbcTemplate.update(SQL_QUERY_FOR_SETTING_STOCK, initialPhone.getId(), INITIAL_STOCK, INITIAL_RESERVED);
        initialCartItem.setPhoneId(initialPhone.getId());
        initialCartItem.setQuantity(INITIAL_RESERVED);
        cart.getCartItems().clear();
        cart.getCartItems().add(initialCartItem);
    }

    private void setPhoneParameters(Phone phone, Long id, String model) {
        phone.setId(id);
        phone.setBrand(PHONE_BRAND);
        phone.setModel(model);
        phoneDao.save(phone);
    }

    @Test
    public void shouldReturnCart() {
        Cart actualCart = cartService.getCart();

        assertEquals(cart.getCartItems(), actualCart.getCartItems());
    }

    @Test
    public void shouldReturnCorrectQuantityOfCartItems() {
        Integer quantity = cartService.getQuantityOfProducts();

        assertEquals(INITIAL_RESERVED, quantity);
    }

    @Test
    public void shouldAddNewCartItem() throws OutOfStockException {
        jdbcTemplate.update(SQL_QUERY_FOR_SETTING_STOCK, secondPhone.getId(), SECOND_STOCK, SECOND_RESERVED);

        cartService.addPhone(secondPhone.getId(), SECOND_STOCK - SECOND_RESERVED);

    }

    @Test
    public void shouldIncreaseQuantityOfExistedCartItem() throws OutOfStockException {
        cartService.addPhone(initialPhone.getId(), INITIAL_STOCK - INITIAL_RESERVED);
    }

    @Test(expected = OutOfStockException.class)
    public void shouldNotAddNewCartItem() throws OutOfStockException{
        jdbcTemplate.update(SQL_QUERY_FOR_SETTING_STOCK, secondPhone.getId(), SECOND_STOCK, SECOND_RESERVED);

        cartService.addPhone(secondPhone.getId(), SECOND_STOCK + SECOND_RESERVED);
    }

    @Test(expected = OutOfStockException.class)
    public void shouldNotIncreaseQuantityOfExistedCartItem() throws OutOfStockException{
        cartService.addPhone(initialPhone.getId(), INITIAL_STOCK + INITIAL_RESERVED);
    }

    @Test
    public void shouldRemove() {
        cartService.remove(initialPhone.getId());

        assertEquals(0, cart.getCartItems().size());
    }
}