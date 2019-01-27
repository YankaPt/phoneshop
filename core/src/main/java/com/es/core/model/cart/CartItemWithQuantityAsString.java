package com.es.core.model.cart;

import java.util.Objects;

public class CartItemWithQuantityAsString {
    private Long phoneId;
    private String quantity;

    public CartItemWithQuantityAsString() {
    }

    public CartItemWithQuantityAsString(Long phoneId, String quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemWithQuantityAsString that = (CartItemWithQuantityAsString) o;
        return Objects.equals(phoneId, that.phoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneId);
    }
}
