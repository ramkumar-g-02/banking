package com.ecommerce.cart.controller;

import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartDTO;
import com.ecommerce.cart.exception.BussinessException;
import com.ecommerce.cart.service.CartService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @CircuitBreaker(name = "cart-service", fallbackMethod = "throwErrorMessage")
//    @Retry(name = "cart-service", fallbackMethod = "throwErrorMessage")
//    @RateLimiter(name = "cart-service", fallbackMethod = "throwErrorMessage")
    public ResponseEntity<Cart> addCart(@Valid @RequestBody CartDTO cartDTO) throws BussinessException {
        Cart cart = cartService.addCart(cartDTO);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    public ResponseEntity<Cart> throwErrorMessage(Exception exception) throws BussinessException {
        throw new BussinessException("Product Service is not available now try again later");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> findCart(@PathVariable Long id) throws BussinessException {
        Cart cart = cartService.findCart(id);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/user-all")
    public ResponseEntity<List<Cart>> findAllUserCart(@RequestParam long userId) {
        List<Cart> cartList = cartService.findAllUserCart(userId);
        return new ResponseEntity<>(cartList, HttpStatus.OK);
    }

    @GetMapping("/increase-cart")
    public ResponseEntity<Cart> increaseCartQuantity(@RequestParam long userId, @RequestParam long productId, @RequestParam int quantity) throws BussinessException {
        Cart cart = cartService.increaseCartQuantity(userId, productId, quantity);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/decrease-cart")
    public ResponseEntity<Cart> decreaseCartQuantity(@RequestParam long userId, @RequestParam long productId, @RequestParam int quantity) throws BussinessException {
        Cart cart = cartService.decreaseCartQuantity(userId, productId, quantity);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteCart(@PathVariable Long id) throws BussinessException {
        Cart cart = cartService.findCart(id);
        String message = null;
        if (cart != null) {
            cartService.deleteCart(id);
            message = "Cart with id "+ id +" deleted sucessfully";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
