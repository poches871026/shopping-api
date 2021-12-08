package com.majestic.shoppingapi.service;


import com.majestic.shoppingapi.dao.MemberDao;
import com.majestic.shoppingapi.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberDao memberDao;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberVO memberVO = new MemberVO();
        memberVO = memberDao.findByEmail(username);
        return createUserDetails(memberVO);
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(MemberVO memberVO) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(memberVO.getAuthority());
        return new User(
                String.valueOf(memberVO.getEmail()),
                memberVO.getPw(),
                Collections.singleton(grantedAuthority)
        );
    }
}