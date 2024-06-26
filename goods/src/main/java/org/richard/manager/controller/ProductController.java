package org.richard.manager.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.richard.manager.controller.payload.UpdateProductPayload;
import org.richard.manager.entity.Product;
import org.richard.manager.service.ProductService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping(("catalogue/products/{productId:\\d+}"))
public class ProductController {

    private final ProductService productService;

    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId).orElseThrow(
                () -> new NoSuchElementException("catalogue.errors.product.not_found")
        );
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
    public String updateProduct(@ModelAttribute(value = "product", binding = false) Product product,
                                @Valid UpdateProductPayload payload,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors",
                    bindingResult
                            .getAllErrors()
                            .stream()
                            .map(ObjectError::getDefaultMessage)
                            .toList());
            return "catalogue/products/edit";
        } else {
            this.productService.updateProduct(product.getId(), payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.getId());
        }
    }

    @PostMapping("delete")
    public String deleteProduct(@PathVariable("productId") int productId) {
        this.productService.deleteProduct(productId);
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e,
                                               Model model,
                                               HttpServletResponse response,
                                               Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
                this.messageSource.getMessage(e.getMessage(), new Object[0], e.getMessage(), locale));
        return "error/404";
    }


}
