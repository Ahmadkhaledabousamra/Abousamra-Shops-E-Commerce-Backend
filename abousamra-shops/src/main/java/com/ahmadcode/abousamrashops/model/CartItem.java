package com.ahmadcode.abousamrashops.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartItem() {
    }

    public CartItem(Product product, int quantity, BigDecimal unitPrice, BigDecimal totalPrice, Cart cart) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.cart = cart;
    }

    public void setTotalPrice(){
        this.totalPrice = this.unitPrice.multiply(new BigDecimal((quantity)));
    }
}
