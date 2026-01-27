package com.example.paymentapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberFormDto {
    @NotEmpty(message = "fullname should not be empty.")
    private String fullname;
    @NotEmpty(message = "nickname should not be empty.")
    private String nickname;
    @NotEmpty(message = "username should not be empty.")
    private String userid;
    @NotEmpty(message = "password should not be empty.")
    private String userpw;
    @NotEmpty(message = "email should not be empty.")
    private String email;
    @NotEmpty(message = "phone number should not be empty.")
    private String phonenum;
    @NotEmpty(message = "adress should not be empty.")
    private String adr;
    private String gender;
    private boolean agreeterms;
    private LocalDate birth;

}
