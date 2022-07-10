package com.alumni.helper;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private  String fullName;

    private  String email;

    private String password;

}
