package com.ecommerce.order.controller;

import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderDTO;
import com.ecommerce.order.exception.BussinessException;
import com.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<Order> addOrder(@Valid @RequestBody OrderDTO orderDTO) throws BussinessException {
        Order order = orderService.addOrder(orderDTO);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrder(@PathVariable Long id) throws BussinessException {
        Order order = orderService.findOrder(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/user-all")
    public ResponseEntity<List<Order>> findAllOrder(@RequestParam long userId) {
        List<Order> orderList = orderService.findAllOrder(userId);
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<Order> updateOrder(@RequestParam long orderId, @RequestParam String status) throws BussinessException {
        Order order = orderService.updateOrder(orderId, status);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) throws BussinessException {
        Order order = orderService.findOrder(id);
        String message = null;
        if (order != null) {
            orderService.deleteOrder(id);
            message = "Order with id "+ id +" deleted sucessfully";
        } else {
            message = "Order with id " + id + " does not exist ";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
