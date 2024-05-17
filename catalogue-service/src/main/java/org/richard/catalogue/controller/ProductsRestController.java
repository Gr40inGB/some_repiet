package org.richard.catalogue.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.richard.catalogue.controller.payload.NewProductPayload;
import org.richard.catalogue.entity.Product;
import org.richard.catalogue.service.ProductService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsRestController {
    private final ProductService productService;

    @GetMapping
    private List<Product> getProducts() {
        return this.productService.findAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NewProductPayload payload,
                                    BindingResult bindingResult,
                                    UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Product product = this.productService.createProduct(payload.title(), payload.details());
            return ResponseEntity.
                    created(uriBuilder.replacePath("catalogue-api/products/" + product.getId()).build().toUri())
                    .body(product);
        }

    }
}

