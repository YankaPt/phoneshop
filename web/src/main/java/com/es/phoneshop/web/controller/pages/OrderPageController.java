package com.es.phoneshop.web.controller.pages;

import com.es.core.exceptions.OutOfStockException;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.order.Customer;
import com.es.core.services.order.OrderService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.validators.CustomerValidator;
import com.es.phoneshop.web.validators.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    private static final String REDIRECTING_ORDER_OVERVIEW_ADDRESS = "redirect:/orderOverview";
    private static final String REDIRECTING_GET_ADDRESS = "redirect:/order";
    private final OrderService orderService;
    private final PhoneService phoneService;
    private final CartService cartService;
    private final TotalPriceService totalPriceService;
    private final CustomerValidator customerValidator;
    private final OrderValidator orderValidator;

    @Autowired
    public OrderPageController(OrderService orderService, PhoneService phoneService, CartService cartService, TotalPriceService totalPriceService, CustomerValidator customerValidator, OrderValidator orderValidator) {
        this.orderService = orderService;
        this.phoneService = phoneService;
        this.cartService = cartService;
        this.totalPriceService = totalPriceService;
        this.customerValidator = customerValidator;
        this.orderValidator = orderValidator;
    }

    @GetMapping
    public String getOrder(Model model) {
        if (model.containsAttribute("errors")) {
            model.addAttribute("errors", (BindingResult) model.asMap().get("errors"));
        }
        List<CartItem> cartItems = cartService.getCart().getCartItems();
        List<Phone> phones = new ArrayList<>();
        model.addAttribute("cartItemsPrice", totalPriceService.getTotalPriceOfProducts());
        cartItems.forEach(cartItem -> phones.add(phoneService.get(cartItem.getPhoneId()).get()));
        model.addAttribute("phones", phones);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("customer", new Customer());
        return "order";
    }

    @PostMapping
    public String placeOrder(Customer customer, RedirectAttributes redirectAttributes) {
        Order order = orderService.createOrder(cartService.getCart());
        DataBinder customerBinder = new DataBinder(customer);
        customerBinder.setValidator(customerValidator);
        if (hasErrors(order, customerBinder, redirectAttributes)) {
            return REDIRECTING_GET_ADDRESS;
        }
        DataBinder orderBinder = new DataBinder(order);
        orderBinder.setValidator(orderValidator);
        if (hasErrors(order, orderBinder, redirectAttributes)) {
            return REDIRECTING_GET_ADDRESS;
        }
        setCustomerInfoAndStatusToOrder(order, customer);
        try {
            orderService.placeOrder(order);
        } catch (OutOfStockException ex) {
            return REDIRECTING_GET_ADDRESS; //fix this stub with message and return order
        }
        cartService.getCart().getCartItems().clear();
        redirectAttributes.addFlashAttribute("orderId", order.getId());
        return REDIRECTING_ORDER_OVERVIEW_ADDRESS;
    }

    private void setCustomerInfoAndStatusToOrder(Order order, Customer customer) {
        order.setFirstName(customer.getFirstName());
        order.setLastName(customer.getLastName());
        order.setDeliveryAddress(customer.getAddress());
        order.setContactPhoneNo(customer.getContactNumber());
        order.setAdditionalInformation(customer.getAdditionalInformation());
        order.setStatus(OrderStatus.NEW);
    }

    private boolean hasErrors(Order order, DataBinder dataBinder, RedirectAttributes redirectAttributes) {
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", dataBinder.getBindingResult());
            redirectAttributes.addFlashAttribute("order", order);
            return true;
        }
        return false;
    }
}
