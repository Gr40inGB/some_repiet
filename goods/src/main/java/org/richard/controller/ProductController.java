package org.richard.controller;

import lombok.RequiredArgsConstructor;
import org.richard.controller.payload.NewProductPayload;
import org.richard.controller.payload.UpdateProductPayload;
import org.richard.entity.Product;
import org.richard.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(("catalogue/products/{productId:\\d+}"))
public class ProductController {

    private final ProductService productService;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId).orElseThrow();
    }

    @GetMapping()
    public String getProduct() {
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage() {
        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute("product") Product product, UpdateProductPayload payload) {
        this.productService.updateProduct(product.getId(), payload.title(), payload.details());
        return "redirect:/catalogue/products/%d".formatted(product.getId());
    }

    @PostMapping("delete")
    public String deleteProduct(@PathVariable("productId") int productId) {
        this.productService.deleteProduct(productId);
        return "redirect:/catalogue/products/list";
    }
}
