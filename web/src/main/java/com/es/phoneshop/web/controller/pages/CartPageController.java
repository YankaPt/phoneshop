package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.CartItem;
import com.es.core.model.cart.CartItemWithQuantityAsString;
import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.validators.CartValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    private static final String REDIRECTING_ADDRESS = "redirect:/cart";
    private final CartService cartService;
    private final TotalPriceService totalPriceService;
    private final PhoneService phoneService;
    private final CartValidator cartValidator;
    private Errors errors;

    @Autowired
    public CartPageController(CartService cartService, TotalPriceService totalPriceService, PhoneService phoneService, CartValidator cartValidator) {
        this.cartService = cartService;
        this.totalPriceService = totalPriceService;
        this.phoneService = phoneService;
        this.cartValidator = cartValidator;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        List<Phone> phones = new ArrayList<>();
        List cartItems;
        if (model.containsAttribute("oldCartItems")) {
            cartItems = (List) model.asMap().get("oldCartItems");
            cartItems.forEach(cartItem -> phones.add(phoneService.get(((CartItemWithQuantityAsString) cartItem).getPhoneId()).get()));
            model.addAttribute("errors", localizeErrors(errors));
        } else {
            cartItems = cartService.getCart().getCartItems();
            cartItems.forEach(cartItem -> phones.add(phoneService.get(((CartItem) cartItem).getPhoneId()).get()));
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("phones", phones);
        model.addAttribute("cartItemsAmount", cartService.getQuantityOfProducts());
        model.addAttribute("cartItemsPrice", totalPriceService.getTotalPriceOfProducts());
        return "cart";
    }

    @PutMapping
    public String updateCart(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        cartService.update(resolveFormData(request.getParameterMap()));
        List cartItems = convertCartItemsMapToList(request.getParameterMap());
        redirectAttributes.addFlashAttribute("oldCartItems", cartItems);
        return REDIRECTING_ADDRESS;
    }

    @PostMapping
    public String deleteItem(@RequestParam(value = "delete") Long phoneIdForDelete) {
        cartService.remove(phoneIdForDelete);
        return REDIRECTING_ADDRESS;
    }

    Map<Long, Integer> resolveFormData(@Validated Map<String, String[]> requestParams) {
        DataBinder dataBinder = new DataBinder(requestParams);
        dataBinder.setValidator(cartValidator);
        dataBinder.validate();
        errors = dataBinder.getBindingResult();
        Map<Long, Integer> result = new HashMap<>();
        requestParams.forEach((paramName, values) -> {
                if (paramName.matches("\\d*") && values!=null) {
                    if (errors.getAllErrors().stream().noneMatch(objectError -> objectError.getCode().matches("quantity"+paramName)))
                    result.put(Long.parseLong(paramName), Integer.parseInt(values[0]));
                }
        });
        return result;
    }

    @ExceptionHandler({NumberFormatException.class})
    public Map<String, Object> handle() {
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, Object> response = new HashMap<>();
        //response.put("message", messageSource.getMessage("invalidInputMessage", null, locale));
        return response;
    }

    List<CartItemWithQuantityAsString> convertCartItemsMapToList(Map<String, String[]> map) {
        List<CartItemWithQuantityAsString> list = new ArrayList<>();
        map.forEach((key, value) -> {
            if (key.matches("\\d*") && value!=null) {
                list.add(new CartItemWithQuantityAsString(Long.parseLong(key), value[0]));
            }
        });
        return list;
    }

    Map<Long, String> localizeErrors(Errors errors) {
        Map<Long, String> localizeErrors = new HashMap<>();
        errors.getAllErrors().forEach((objectError -> {
            localizeErrors.put(Long.parseLong(objectError.getCode().substring(8)), objectError.getDefaultMessage());
        }));
        return localizeErrors;
    }

    Errors getErrors() {
        return errors;
    }
}
