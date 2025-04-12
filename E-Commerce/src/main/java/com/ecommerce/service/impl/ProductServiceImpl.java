package com.ecommerce.service.impl;

import com.ecommerce.entity.Product;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductService;
import com.ecommerce.utility.ServiceUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ServiceUtil serviceUtil;

    @Override
    public Product addProduct(Product product) {
        String pid = serviceUtil.generateId("P");
        product.setPid(pid);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String pid, Product product) {
        Product existingProduct = productRepository.findById(pid).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDetail(product.getDetail());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setIsAvailable(product.getIsAvailable());
        existingProduct.setImage(product.getImage());
        return productRepository.save(existingProduct);
    }

    @Override
    public Product updateProductPrice(String pid, Double price) {
        Product existingProduct = productRepository.findById(pid).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        existingProduct.setPrice(price);
        return productRepository.save(existingProduct);
    }

    @Override
    public Product updateProductImage(String pid, byte[] newImage) {
        return null;
    }

    @Override
    public Product updateProductQuantity(String pid, int quantity) {
        Product existingProduct = productRepository.findById(pid).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        existingProduct.setQuantity(quantity);
        return productRepository.save(existingProduct);
    }

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByUserid(String userid) {
        List<Product> productList = productRepository.findByUserUserid(userid);
        if (productList == null || productList.isEmpty()) {
            throw new ResourceNotFoundException("Product not found!");
        }

        return productList;
    }

    @Override
    public List<String> getAllProductCategory() {
        List<String> productList =productRepository.findAllCategories();
        if (productList == null) {
            throw new ResourceNotFoundException("Product not found!");
        }

        return productList;
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        category.trim().toLowerCase();
        List<Product> productList =productRepository.findAllByCategory(category);
        if (productList == null) {
            throw new ResourceNotFoundException("Product not found!");
        }

        return productList;
    }

    @Override
    public List<Product> getProductBySearch(String search) {
        search.trim().toLowerCase();
        List<Product> productList =productRepository.findByNameContainingIgnoreCase(search);
        if (productList == null) {
            throw new ResourceNotFoundException("Product not found!");
        }

        return productList;
    }

    @Override
    public Product getProductById(String pid) {
        return productRepository.findById(pid).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public int getProductQuantity(String pid) {
        return productRepository.getQuantityByPid(pid);
    }

    @Override
    public byte[] getProductImage(String pid) {
        return productRepository.findImageByPid(pid);
    }

    @Override
    public void removeProduct(String pid) {
        Product product = productRepository.findById(pid).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        product.setIsAvailable(0);
        productRepository.save(product);
    }

    @Override
    public boolean isProductAvailable(String pid) {
        int available = productRepository.findIsAvailableByPid(pid);
        return available == 1;
    }
}
