package com.ecommerce.service;

import com.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);
    Product updateProduct(String pid, Product product);
    Product updateProductPrice(String pid, Double price);
    Product updateProductImage(String pid, byte[] newImage);
    Product updateProductQuantity(String pid, int quantity);
    List<Product> getAllProducts();
    List<Product> getAllProductsByUserid(String userid);
    List<String> getAllProductCategory();
    List<Product> getProductByCategory(String category);
    List<Product> getProductBySearch(String search);
    Product getProductById(String pid);
    int getProductQuantity(String pid);
    boolean setProductQuantity(String pid, int qty);
    byte[] getProductImage(String pid);
    void removeProduct(String pid);
    boolean isProductAvailable(String pid);
    void setProductAvailable(String pid, int qty);
}
