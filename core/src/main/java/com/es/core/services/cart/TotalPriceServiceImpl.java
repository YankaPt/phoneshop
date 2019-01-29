package com.es.core.services.cart;

import com.es.core.dao.PhoneDao;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TotalPriceServiceImpl implements TotalPriceService {
    private final PhoneDao phoneDao;

    @Autowired
    public TotalPriceServiceImpl(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    @Override
    public BigDecimal getTotalPriceOfProducts(Cart cart) {
        BigDecimal totalCartPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getCartItems()) {
            totalCartPrice = totalCartPrice.add(phoneDao.get(cartItem.getPhoneId()).get().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        return totalCartPrice;
    }
}
