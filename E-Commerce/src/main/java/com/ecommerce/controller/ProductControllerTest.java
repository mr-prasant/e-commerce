package com.ecommerce.controller;

import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductControllerTest {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct() {
        return new ResponseEntity<>(
                productService.getAllProducts(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String pid) {
        return new ResponseEntity<>(
                productService.getProductById(pid),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/category")
    public ResponseEntity<List<String>> getAllProductCategory() {
        return new ResponseEntity<>(
                productService.getAllProductCategory(),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getAllProductByCategory(@PathVariable String category) {
        return new ResponseEntity<>(
                productService.getProductByCategory(category),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<List<Product>> getAllProductBySearch(@PathVariable String search) {
        return new ResponseEntity<>(
                productService.getProductBySearch(search),
                HttpStatus.FOUND
        );
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return new ResponseEntity<>(
                productService.addProduct(product),
                HttpStatus.FOUND
        );
    }

    @PutMapping("/{pid}")
    public ResponseEntity<Product> updateProduct(@PathVariable String pid, @RequestBody Product product) {
        return new ResponseEntity<>(
                productService.updateProduct(pid, product),
                HttpStatus.FOUND
        );
    }

}
