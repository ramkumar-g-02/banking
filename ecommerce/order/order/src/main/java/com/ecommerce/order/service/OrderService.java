package com.ecommerce.order.service;

import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderDTO;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.entity.PaymentMethod;
import com.ecommerce.order.exception.BussinessException;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.util.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplateBuilder restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private WebClient.Builder builder;

    public Order addOrder(OrderDTO orderDTO) throws BussinessException {
//        ServiceInstance serviceInstance = loadBalancerClient.choose("product-service");
//        String domainURL = serviceInstance.getUri().toString();
        Optional<PaymentMethod> optionalPaymentMethod = Arrays.stream(PaymentMethod.values())
                .filter(paymentMethod -> paymentMethod.toString().equalsIgnoreCase(orderDTO.getPaymentMethod())).findFirst();
        if (optionalPaymentMethod.isEmpty()) {
            throw new BussinessException("Invalid Payment Method");
        }
        Object cart = builder.build().get().uri("http://cart-service/cart/"+orderDTO.getCartId()).retrieve().bodyToMono(Object.class).block();
        JsonNode cartJsonNode = JSONUtil.getJSONNode(cart);
        long productId = cartJsonNode.get("productId").asLong();
        long userId = cartJsonNode.get("userId").asLong();

        Object product = builder.build().get().uri("http://product-service/product/"+productId).retrieve().bodyToMono(Object.class).block();
//        Object product = restTemplate.getForObject(domainURL + "/product/" + orderDTO.getProductId(), Object.class);
//        Object product = restTemplate.getForObject("http://product-service/product/" + orderDTO.getProductId(), Object.class);
        JsonNode productJsonNode = JSONUtil.getJSONNode(product);
        boolean inStock = productJsonNode.get("inStock").asBoolean();
        double price = productJsonNode.get("price").asDouble();
        if (!inStock) {
            throw new BussinessException("No Stock");
        }
        Object savedProduct = builder.build().get().uri("http://product-service/product/quantity?quantity="+orderDTO.getQuantity()+"&productId="+productId).retrieve().bodyToMono(Object.class).block();
//        restTemplate.build().getForObject ("http://product-service/product/quantity?quantity="+orderDTO.getQuantity()+"&productId="+orderDTO.getProductId(), Object.class);
        Order order = Order.builder()
                .productId(productId)
                .status(OrderStatus.PENDING.toString())
                .paymentMethod(optionalPaymentMethod.get().toString())
                .orderOn(LocalDateTime.now())
                .deliveryDate(LocalDate.now().plusDays(5))
                .quantity(orderDTO.getQuantity())
                .totalPrice(price * orderDTO.getQuantity())
                .userId(userId)
                .build();
        orderRepository.save(order);
        builder.build().get().uri("http://cart-service/cart/delete/"+orderDTO.getCartId()).retrieve().bodyToMono(String.class).block();
        return order;
    }

    public Order findOrder(Long id) throws BussinessException {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new BussinessException("Order with id " + id + " does not exist");
        }
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }


    public List<Order> findAllOrder(long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrder(long orderId, String status) throws BussinessException {
        Order order = findOrder(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
