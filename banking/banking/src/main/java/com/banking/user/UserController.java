package com.banking.user;

import com.banking.exception.BussinessException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserDTO userDTO) throws BussinessException {
        User user = userService.addUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUser(@PathVariable Long id) throws BussinessException {
        User user = userService.findUser(id);
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
            message = "User " + user.getUserName() + " with id "+ id +" deleted sucessfully";
        } else {
            message = "User with id " + id + " does not exist ";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
