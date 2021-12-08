package com.majestic.shoppingapi.service;

import com.majestic.shoppingapi.dao.MemberDao;
import com.majestic.shoppingapi.dto.MemberResponseDto;
import com.majestic.shoppingapi.repository.MemberRepository;
import com.majestic.shoppingapi.util.SecurityUtil;
import com.majestic.shoppingapi.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    private MemberDao memberDao;

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(String email) {
        MemberResponseDto result = new MemberResponseDto();
//        return memberRepository.findByEmail(email)
//                .map(MemberResponseDto::of)
//                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
        return result;
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo() {

        MemberResponseDto result = new MemberResponseDto();

        String username = SecurityUtil.getCurrentMemberId();
        System.out.println("SecurityUtil.getCurrentMemberId() ::: " + SecurityUtil.getCurrentMemberId());
        result = memberDao.findById(username);
        System.out.println("MemberService getMyInfo result :: " + result);
        if(result.getEmail() == null) {
            new RuntimeException("로그인 유저 정보가 없습니다.");
        }
        /*
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
        */
        return result;
    }

    public MemberResponseDto getMemberList() {
        MemberResponseDto memberResponseDto = new MemberResponseDto();

        List<MemberVO> memberVO = memberDao.getMemberList();
        memberResponseDto.setList(memberVO);

        return memberResponseDto;
    }
}