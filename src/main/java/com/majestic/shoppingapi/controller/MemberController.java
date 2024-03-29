package com.majestic.shoppingapi.controller;


import com.majestic.shoppingapi.dto.MemberResponseDto;
import com.majestic.shoppingapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {

        return ResponseEntity.ok(memberService.getMyInfo());
    }

    @GetMapping("/{email}")
    public ResponseEntity<MemberResponseDto> getMemberInfo(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getMemberInfo(email));
    }

    @GetMapping("/list")
    public ResponseEntity<MemberResponseDto> getMemberList() {

        MemberResponseDto memberResponseDto = memberService.getMemberList();

        System.out.println("MemberResponseDto :: " + memberResponseDto);
        return ResponseEntity.ok(memberResponseDto);
    }

    @GetMapping("/list2")
    public MemberResponseDto getMemberList2() {

        MemberResponseDto memberResponseDto = memberService.getMemberList();

        System.out.println("MemberResponseDto2222 :: " + memberResponseDto);
        return memberResponseDto;
    }

}