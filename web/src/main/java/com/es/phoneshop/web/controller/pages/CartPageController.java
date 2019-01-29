package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.CartItem;
import com.es.core.model.cart.CartItemWithQuantityAsString;
import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.services.CartFormResolver;
import com.es.phoneshop.web.services.CartItemsConverter;
import com.es.phoneshop.web.services.ErrorLocalizer;
import com.es.phoneshop.web.services.ErrorsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    private static final String REDIRECTING_ADDRESS = "redirect:/cart";

    private final CartService cartService;
    private final TotalPriceService totalPriceService;
    private final PhoneService phoneService;
    private final CartFormResolver cartFormResolver;
    private final ErrorLocalizer errorLocalizer;
    private final CartItemsConverter cartItemsConverter;

    @Autowired
    public CartPageController(CartService cartService, TotalPriceService totalPriceService, PhoneService phoneService, CartFormResolver cartFormResolver, ErrorLocalizer errorLocalizer, CartItemsConverter cartItemsConverter) {
        this.cartService = cartService;
        this.totalPriceService = totalPriceService;
        this.phoneService = phoneService;
        this.cartFormResolver = cartFormResolver;
        this.errorLocalizer = errorLocalizer;
        this.cartItemsConverter = cartItemsConverter;
    }

    @GetMapping
    public String getCart(Model model, Authentication authentication) {
        List<Phone> phones = new ArrayList<>();
        List cartItems;
        if (model.containsAttribute("oldCartItems")) {
            cartItems = (List) model.asMap().get("oldCartItems");
            cartItems.forEach(cartItem -> phones.add(phoneService.get(((CartItemWithQuantityAsString) cartItem).getPhoneId()).get()));
            Map<Long, String> localizedErrors = errorLocalizer.localizeErrors((Errors) model.asMap().get("errors"), (Locale) model.asMap().get("locale"));
            model.addAttribute("errors", localizedErrors);
        } else {
            cartItems = cartService.getCart().getCartItems();
            cartItems.forEach(cartItem -> phones.add(phoneService.get(((CartItem) cartItem).getPhoneId()).get()));
        }
        if (cartItems.isEmpty()) {
            return "emptyCart";
        }
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("userName", authentication.getName());
        } else {
            model.addAttribute("userName", null);
        }
        model.addAttribute("cartItemsAmount", cartService.getQuantityOfProducts());
        model.addAttribute("cartItemsPrice", totalPriceService.getTotalPriceOfProducts(cartService.getCart()));
        model.addAttribute("phones", phones);
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @PutMapping
    public String updateCart(@RequestParam Map<String, String> formData, RedirectAttributes redirectAttributes, Locale locale) {
        ErrorsWrapper errorsWrapper = new ErrorsWrapper();
        cartService.update(cartFormResolver.resolveFormData(formData, errorsWrapper));
        List<CartItemWithQuantityAsString> cartItems = cartItemsConverter.convertCartItemsMapToList(formData);
        redirectAttributes.addFlashAttribute("oldCartItems", cartItems);
        redirectAttributes.addFlashAttribute("errors", errorsWrapper.getErrors());
        redirectAttributes.addFlashAttribute("locale", locale);
        return REDIRECTING_ADDRESS;
    }

    @PostMapping
    public String deleteItem(@RequestParam(value = "delete") Long phoneIdForDelete) {
        cartService.remove(phoneIdForDelete);
        return REDIRECTING_ADDRESS;
    }
}
