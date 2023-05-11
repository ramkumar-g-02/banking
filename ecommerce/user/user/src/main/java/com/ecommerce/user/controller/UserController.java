package com.ecommerce.user.controller;

import com.ecommerce.user.configuration.JWTTokenService;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserDTO;
import com.ecommerce.user.exception.BussinessException;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTTokenService jwtTokenService;

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserDTO userDTO) throws BussinessException {
        User user = userService.addUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password) throws BussinessException {
        User user = userService.login(email, password);
        String token = jwtTokenService.generateToken(user);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Login Successful");
        map.put("token", token);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUser(@PathVariable Long id, @RequestHeader("email") String email) throws BussinessException {
        User user = userService.findUser(id, email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAllUser() {
        List<User> userList = userService.findAllUser();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<User> findUser(@RequestParam(name = "id") String id) throws BussinessException {
        User user = userService.findUser(Long.parseLong(id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.updateUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws BussinessException {
        User user = userService.findUser(id);
        String message = null;
        if (user != null) {
            userService.deleteUser(id);
            message = "User " + user.getName() + " with id " + id + " deleted sucessfully";
        } else {
            message = "User with id " + id + " does not exist ";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


}
