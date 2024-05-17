package org.richard.catalogue.service;

import lombok.RequiredArgsConstructor;
import org.richard.catalogue.entity.Product;
import org.richard.catalogue.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(String title, String details) {
        return productRepository.save(new Product(null, title, details));
    }

    @Override
    public void updateProduct(Integer id, String title, String details) {
        this.productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    product.setTitle(title);
                    product.setDetails(details);
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    public void deleteProduct(Integer productId) {
        this.productRepository.deleteById(productId);
    }

    @Override
    public Optional<Product> findProduct(Integer productId) {
        return this.productRepository.findById(productId);
    }
}
