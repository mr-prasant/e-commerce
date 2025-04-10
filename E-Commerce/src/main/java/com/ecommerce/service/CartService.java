package com.ecommerce.service;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;

import java.util.List;

interface CartService {
    List<Cart> getAllCartProducts(String userid);
    Product addProductToCart(Cart cart);
    void removeProductFromCart(String pid, Cart cart);
    int updateCartProduct(String pid, int quantity);
    int cartSize(String userid, String pid);
    boolean removeAllCartProducts(String userid);
}
