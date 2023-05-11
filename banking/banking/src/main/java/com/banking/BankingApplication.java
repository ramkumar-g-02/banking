package com.banking;

import com.banking.user.Student;
import com.banking.user.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class BankingApplication {
    @Autowired
    private StudentRepo studentRepo;



    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }


    @GetMapping("/call/{name}")
    public void call(@PathVariable String name) {
        List<Student> java = studentRepo.findByName(name);
        System.out.println(java);
    }


}
