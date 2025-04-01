package com.example.shareEdu.entity;
import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String userName;

    String password;
    String firstName;
    LocalDate dob;
    String lastName;

    @Column(nullable = false)
    boolean isDeleted = false;

    String email;
    String address;
    String introduction;
    LocalDate createDate ;
    LocalDate updateDate;
    @ManyToMany
    Set<Role> roles;
}