package com.catboardback.dto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AccountCredentials {
    // id pw 묶어놓은 dto
    private final String email;
    private final String password;
}