package com.majestic.shoppingapi.dao;

import com.majestic.shoppingapi.vo.RefreshTokenVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenDao {


    void insertRefreshToken(RefreshTokenVO refreshTokenVO);

    RefreshTokenVO findByUsername(String name);

    void refreshTokenUpdate(RefreshTokenVO refreshTokenVO);

    String findByKey(String key);

    void updateRefreshToken(RefreshTokenVO refreshTokenVO);
}
