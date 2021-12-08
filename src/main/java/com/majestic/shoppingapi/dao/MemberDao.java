package com.majestic.shoppingapi.dao;


import com.majestic.shoppingapi.dto.MemberResponseDto;
import com.majestic.shoppingapi.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberDao {

    List<MemberVO> getMemberList();

    String existsByEmail(String email);

    int save(MemberVO memberVO);

    MemberVO findByEmail(String username);

    MemberResponseDto findById(String username);
}
