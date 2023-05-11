package com.ecommerce.product.controller;

import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.ProductDTO;
import com.ecommerce.product.exception.BussinessException;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = productService.addProduct(productDTO);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = productService.updateProduct(productDTO);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProduct(@PathVariable Long id) throws BussinessException {
        Product product = productService.findProduct(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAllProduct() {
        List<Product> productList = productService.findAllProduct();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Product>> findAllProductByCategory(@RequestParam String category, @RequestParam(required = false) boolean inStock) {
        List<Product> productList = productService.findAllProductByCategory(category, inStock);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/quantity")
    public ResponseEntity<Product> updateQuantity(@RequestParam long productId, @RequestParam int quantity) throws BussinessException {
        Product product = productService.updateQuantity(productId, quantity);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) throws BussinessException {
        Product product = productService.findProduct(id);
        String message = null;
        if (product != null) {
            productService.deleteProduct(id);
            message = "Product " + product.getName() + " with id "+ id +" deleted sucessfully";
        } else {
            message = "Product with id " + id + " does not exist ";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
