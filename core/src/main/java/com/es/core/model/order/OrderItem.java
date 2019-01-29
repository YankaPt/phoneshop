package com.es.core.model.order;

import com.es.core.model.phone.Phone;

import java.util.Objects;

public class OrderItem {
    private Phone phone;
    private Order order;
    private Integer quantity;

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(final Phone phone) {
        this.phone = phone;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(phone, orderItem.phone) &&
                Objects.equals(order, orderItem.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, order);
    }
}
