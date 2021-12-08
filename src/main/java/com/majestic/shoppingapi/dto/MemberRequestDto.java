package com.majestic.shoppingapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    private String email;

    private String password;

    private String memberId;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}