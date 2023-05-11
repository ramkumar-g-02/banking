package com.ecommerce.user.service;

import com.ecommerce.user.entity.Address;
import com.ecommerce.user.entity.Role;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserDTO;
import com.ecommerce.user.exception.BussinessException;
import com.ecommerce.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User addUser(UserDTO userDTO) throws BussinessException {
        User existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            throw new BussinessException("User already exist with Email - " + userDTO.getEmail());
        }
        User user = new User();
        setUserValues(userDTO, user);
        user.setCreatedOn(LocalDateTime.now());
        user.setModifiedOn(LocalDateTime.now());
        user.setRole(Role.USER);
        userRepository.save(user);
        return user;
    }

    public User findUser(Long id) throws BussinessException {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new BussinessException("User with id " + id + " does not exist");
        }
    }

    public User findUser(Long id, String email) throws BussinessException {
        Optional<User> optional = userRepository.findByUserIdAndEmail(id, email);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new BussinessException("User does not exist");
        }
    }

    public User findByEmail(String email) throws BussinessException {
        User user  = userRepository.findByEmail(email);
        if (user == null) {
            throw new BussinessException("User with email - " + email + " does not exist");
        }
        return user;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        setUserValues(userDTO, user);
        userRepository.save(user);
        return user;
    }

    private void setUserValues(UserDTO userDTO, User user) {
        Address newAddress = userDTO.getAddress();
        Address oldAddress = user.getAddress() != null ? user.getAddress() : new Address();
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (newAddress != null) {
            if (newAddress.getCity() != null) {
                oldAddress.setCity(newAddress.getCity());
            }
            if (newAddress.getBuildingNumber() != null) {
                oldAddress.setBuildingNumber(newAddress.getBuildingNumber());
            }
            if (newAddress.getPinCode() != null) {
                oldAddress.setPinCode(newAddress.getPinCode());
            }
            if (newAddress.getStreetName() != null) {
                oldAddress.setStreetName(newAddress.getStreetName());
            }
            user.setAddress(oldAddress);
        }
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User login(String email, String password) throws BussinessException {
        User user = findByEmail(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BussinessException("Check your email and password");
        }
        return user;
    }
}
