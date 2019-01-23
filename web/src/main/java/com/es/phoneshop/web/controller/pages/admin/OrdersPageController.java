package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.services.order.Customer;
import com.es.core.services.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    private static final String ORDERS_ATTRIBUTE = "orders";
    private static final String ADMIN_ORDERS_ADDRESS = "admin/orders";
    private static final String ADMIN_ORDER_OVERVIEW_ADDRESS = "admin/orderOverview";
    private static final String REDIRECTING_ORDER_OVERVIEW_ADDRESS = "redirect:/admin/orders";
    private final OrderService orderService;

    @Autowired
    public OrdersPageController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrders(Model model) {
        model.addAttribute(ORDERS_ATTRIBUTE, orderService.getAllOrders());
        return ADMIN_ORDERS_ADDRESS;
    }

    @GetMapping("/{orderId}")
    public String getOrderOverview(@PathVariable long orderId, Model model) {
        Order order = orderService.getOrder(orderId);
        Customer customer = new Customer();
        customer.setFirstName(order.getFirstName());
        customer.setLastName(order.getLastName());
        customer.setAddress(order.getDeliveryAddress());
        customer.setContactNumber(order.getContactPhoneNo());
        customer.setAdditionalInformation(order.getAdditionalInformation());
        model.addAttribute("customer", customer);
        model.addAttribute("order", order);
        return ADMIN_ORDER_OVERVIEW_ADDRESS;
    }

    @PostMapping("/{orderId}")
    public String processButton(@PathVariable long orderId, @RequestParam(required = false) String delivered, @RequestParam(required = false) String rejected) {
        Order order = orderService.getOrder(orderId);
        if (delivered != null) {
            order.setStatus(OrderStatus.DELIVERED);

        } else if (rejected != null) {
            order.setStatus(OrderStatus.REJECTED);
        }
        orderService.updateOrder(order);
        return REDIRECTING_ORDER_OVERVIEW_ADDRESS + "/" + orderId;
    }
}
