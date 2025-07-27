package com.musdon.banking_project.dto;

import lombok.*;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String firstName;

    private String lastName;

    private String gender;

    private Integer age;

    private String address;

    private String stateOfOrigin;

    private String email;

    private String phoneNumber;

    private String alternativePhoneNumber;

}
