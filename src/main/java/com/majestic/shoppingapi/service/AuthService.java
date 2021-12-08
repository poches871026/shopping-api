package com.majestic.shoppingapi.service;

import com.majestic.shoppingapi.dao.MemberDao;
import com.majestic.shoppingapi.dao.TokenDao;
import com.majestic.shoppingapi.dto.MemberRequestDto;
import com.majestic.shoppingapi.dto.MemberResponseDto;
import com.majestic.shoppingapi.dto.TokenDto;
import com.majestic.shoppingapi.dto.TokenRequestDto;
import com.majestic.shoppingapi.jwt.TokenProvider;
import com.majestic.shoppingapi.repository.MemberRepository;
import com.majestic.shoppingapi.repository.RefreshTokenRepository;
import com.majestic.shoppingapi.vo.MemberVO;
import com.majestic.shoppingapi.vo.RefreshTokenVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private TokenDao tokenDao;

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        MemberResponseDto result = new MemberResponseDto();

        String chk = memberDao.existsByEmail(memberRequestDto.getEmail());
        if (chk.equals("Y")) {
            result.setCode("FAIL");
            result.setMessage("이미 가입되어 있는 유저입니다.");
        } else {
            String pw = memberRequestDto.getPassword();
            pw = passwordEncoder.encode(pw);
            System.out.println("pw ::: " + pw);
            memberRequestDto.setPassword(pw);
            MemberVO memberVO = new MemberVO();
            memberVO.setEmail(memberRequestDto.getEmail());
            memberVO.setPw(pw);
            memberVO.setMemberId(memberRequestDto.getMemberId());
            System.out.println("1111");
            int res = memberDao.save(memberVO);
            System.out.println("2222 ::: " + res);
            if (res > 0) {
                result.setCode("OK");
            } else {
                result.setCode("ERROR");
            }
        }

        return result;
    }

    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //System.out.println("11111111111111111111");
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        //System.out.println("11111111111111111111 tokenDto :: " + tokenDto);
        // 4. RefreshToken 저장
//        RefreshToken refreshToken = RefreshToken.builder()
//                .key(authentication.getName())
//                .value(tokenDto.getRefreshToken())
//                .build();
        RefreshTokenVO refreshTokenVO = new RefreshTokenVO();
        refreshTokenVO.setKey(authentication.getName());
        refreshTokenVO.setValue(tokenDto.getRefreshToken());


        //System.out.println("222222222222222222");
        //refreshTokenRepository.save(refreshToken);

        // 토큰이 있으면 업데이트, 없으면 인서트
        String chk = tokenDao.findByKey(refreshTokenVO.getKey());
        if(chk.equals("Y")){
            tokenDao.refreshTokenUpdate(refreshTokenVO);
        }else {
            tokenDao.insertRefreshToken(refreshTokenVO);
        }


        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
//                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        RefreshTokenVO refreshTokenVO = tokenDao.findByUsername(authentication.getName());

        System.out.println("AuthService refreshTokenVO :: " + refreshTokenVO);
        System.out.println("AuthService tokenRequestDto.getRefreshToken :: " + tokenRequestDto.getRefreshToken());

        // 4. Refresh Token 일치하는지 검사
        if (!refreshTokenVO.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        refreshTokenVO.setValue(tokenDto.getRefreshToken());
        //refreshTokenRepository.save(newRefreshToken);
        System.out.println("tokenDao.refreshTokenUpdate :: " + refreshTokenVO);
        tokenDao.refreshTokenUpdate(refreshTokenVO);

        // 토큰 발급
        return tokenDto;
    }
}