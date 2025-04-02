package com.medium.eric.EricProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medium.eric.EricProject.dto.Product;
import com.medium.eric.EricProject.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Integer id) {
    	 System.out.println("getProductById in service called with id: " + id); // Add logging
         // ... your service logic ...
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Integer id, Product productDetails) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            Product existingProduct = product.get();
            existingProduct.setProductName(productDetails.getProductName());
            existingProduct.setProductCost(productDetails.getProductCost());
            existingProduct.setProductAvailable(productDetails.isProductAvailable());
            return productRepository.save(existingProduct);
        } else {
            return null; // Or throw an exception
        }
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
