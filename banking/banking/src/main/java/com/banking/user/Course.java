package com.banking.user;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Course {

    @Id
    private Long id;

    @ManyToMany(mappedBy = "likedCourses", fetch = FetchType.LAZY)
    private Set<Student> likes;

    private String name;

}