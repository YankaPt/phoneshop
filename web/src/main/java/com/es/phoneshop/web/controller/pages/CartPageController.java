package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    private static final String REDIRECTING_ADDRESS = "redirect:/cart";
    private final CartService cartService;
    private final TotalPriceService totalPriceService;
    private final PhoneService phoneService;

    @Autowired
    public CartPageController(CartService cartService, TotalPriceService totalPriceService, PhoneService phoneService) {
        this.cartService = cartService;
        this.totalPriceService = totalPriceService;
        this.phoneService = phoneService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        List<CartItem> cartItems = cartService.getCart().getCartItems();
        if (cartItems.isEmpty()) {
            return "emptyCart";
        }
        List<Phone> phones = new ArrayList<>();
        model.addAttribute("cartItemsAmount", cartService.getQuantityOfProducts());
        model.addAttribute("cartItemsPrice", totalPriceService.getTotalPriceOfProducts());
        cartItems.forEach(cartItem -> phones.add(phoneService.get(cartItem.getPhoneId()).get()));
        model.addAttribute("phones", phones);
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @PutMapping
    public String updateCart(HttpServletRequest request) {
        Errors errors = null;
        cartService.update(resolveFormData(request, errors));
        return REDIRECTING_ADDRESS;
    }

    @PostMapping
    public String deleteItem(@RequestParam(value = "delete") Long phoneIdForDelete) {
        cartService.remove(phoneIdForDelete);
        return REDIRECTING_ADDRESS;
    }

    Map<Long, Integer> resolveFormData(HttpServletRequest request, Errors errors) {
        Map<Long, Integer> result = new HashMap<>();
        Map<String, String[]> params = request.getParameterMap();
        params.forEach((paramName, values) -> {
            if (paramName.contains("quantity")) {
                result.put(Long.parseLong(paramName.substring("quantity".length())), Integer.parseInt(values[0]));
            }
        });
        return result;
    }
}
