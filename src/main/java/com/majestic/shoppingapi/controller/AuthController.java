package com.majestic.shoppingapi.controller;


import com.majestic.shoppingapi.dto.MemberRequestDto;
import com.majestic.shoppingapi.dto.MemberResponseDto;
import com.majestic.shoppingapi.dto.TokenDto;
import com.majestic.shoppingapi.dto.TokenRequestDto;
import com.majestic.shoppingapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = authService.signup(memberRequestDto);
        return ResponseEntity.ok(memberResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto memberRequestDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        System.out.println("loginloginloginloginloginloginlogin :: " + memberRequestDto);

        TokenDto tokenDto = authService.login(memberRequestDto);
        httpHeaders.add("accessToken", tokenDto.getAccessToken());
        httpHeaders.add("refreshToken", tokenDto.getRefreshToken());

        System.out.println("httpHeaders ::: " + httpHeaders);

        System.out.println("ResponseEntity.ok(tokenDto) ::: " + ResponseEntity.ok(tokenDto.toString()));

        return ResponseEntity.ok().headers(httpHeaders).body(tokenDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

}