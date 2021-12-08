package com.majestic.shoppingapi.dto;


import com.majestic.shoppingapi.vo.MemberVO;
import lombok.Data;

import java.util.List;

@Data
public class MemberResponseDto {

    private String email;

    private String code;

    private String message;

    private List<MemberVO> list;

    /*
    public MemberResponseDto(String email) {
    }


    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getEmail());
    }
    */


}