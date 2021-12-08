package com.majestic.shoppingapi.vo;


import lombok.Data;

@Data
public class MemberVO {

    private String email;

    private Integer seq;

    private String memberId;

    private String pw;

    private String authority;


}
