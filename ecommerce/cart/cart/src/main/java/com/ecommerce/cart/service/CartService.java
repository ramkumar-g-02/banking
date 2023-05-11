package com.ecommerce.cart.service;

import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartDTO;
import com.ecommerce.cart.exception.BussinessException;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.cart.util.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${server.port}")
    private String port;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private WebClient.Builder builder;

    public Cart addCart(CartDTO cartDTO) throws BussinessException {
//        ServiceInstance serviceInstance = loadBalancerClient.choose("product-service");
//        String domainURL = serviceInstance.getUri().toString();
//        Object product = restTemplate.getForObject(domainURL + "/product/" + cartDTO.getProductId(), Object.class);
//        Object product = restTemplate.getForObject("http://product-service/product/" + cartDTO.getProductId(), Object.class);
        Object product = builder.build().get().uri("http://product-service/product/"+cartDTO.getProductId()).retrieve().bodyToMono(Object.class).block();
        JsonNode jsonNode = JSONUtil.getJSONNode(product);
        boolean inStock = jsonNode.get("inStock").asBoolean();
        double price = jsonNode.get("price").asDouble();
        if (!inStock) {
            throw new BussinessException("No Stock");
        }
        Cart cart = Cart.builder()
                .userId(cartDTO.getUserId())
                .quantity(1)
                .price(price)
                .productId(cartDTO.getProductId())
                .createdOn(LocalDateTime.now())
                .modifiedOn(LocalDateTime.now())
                .build();
        cartRepository.save(cart);
        return cart;
    }

    public Cart findCart(Long id) throws BussinessException {
        Optional<Cart> optional = cartRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new BussinessException("Cart with id " + id + " does not exist " + port);
        }
    }

    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    public List<Cart> findAllCart() {
        return cartRepository.findAll();
    }

    public List<Cart> findAllUserCart(long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart increaseCartQuantity(long userId, long productId, int quantity) throws BussinessException {
        Cart cart = cartRepository.findByUserIdAndProductId(userId, productId);
        if (quantity <= 0) {
            throw new BussinessException("Quantity should be greater than 0 ");
        }
        cart.setQuantity(cart.getQuantity() + quantity);
        return cartRepository.save(cart);
    }

    public Cart decreaseCartQuantity(long userId, long productId, int quantity) throws BussinessException {
        Cart cart = cartRepository.findByUserIdAndProductId(userId, productId);
        if (quantity > cart.getQuantity()) {
            throw new BussinessException("You are having only " + cart.getQuantity() + " quantity");
        }
        cart.setQuantity(cart.getQuantity() - quantity);
        return cartRepository.save(cart);
    }

}
