package com.musdon.banking_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // primary key.

    private String firstName;

    private String lastName;

    private String gender;

    private Integer age;

    private String address;

    private String stateOfOrigin;

    private String accountNumber; // this account number will be created on creation of user

    private BigDecimal accountBalance;

    private String email;

    private String phoneNumber;

    private String alternativePhoneNumber;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
