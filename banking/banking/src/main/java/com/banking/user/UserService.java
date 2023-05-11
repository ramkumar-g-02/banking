package com.banking.user;

import com.banking.exception.BussinessException;
import com.banking.piggybank.PiggyBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(UserDTO userDTO) throws BussinessException {
        User existingUser = userRepository.findByUserName(userDTO.getUserName());
        if (existingUser != null) {
            throw new BussinessException("User already exist with User Name - " + userDTO.getUserName());
        }
        existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            throw new BussinessException("User already exist with Email - " + userDTO.getEmail());
        }
        User user = new User();
        setUserValues(userDTO, user);
        user.setCreatedOn(LocalDateTime.now());
        user.setLastModifiedOn(LocalDateTime.now());
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

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(UserDTO userDTO) {
        User user = userRepository.findByUserName(userDTO.getUserName());
        setUserValues(userDTO, user);
        userRepository.save(user);
        return user;
    }

    private static void setUserValues(UserDTO userDTO, User user) {
        if (userDTO.getUserName() != null) {
            user.setUserName(userDTO.getUserName());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getGender() != null) {
            user.setGender(userDTO.getGender());
        }
        if (userDTO.getMobileNumber() != null) {
            user.setMobileNumber(userDTO.getMobileNumber());
        }
        if (userDTO.getDateOfBirth() != null) {
            user.setDateOfBirth(userDTO.getDateOfBirth());
        }
        user.setLastModifiedOn(LocalDateTime.now());
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

}
