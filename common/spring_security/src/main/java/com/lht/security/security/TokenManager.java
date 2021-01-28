package com.lht.security.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lhtao
 * @date 2021/1/26 17:31
 */
@Component
public class TokenManager {

    //token有效时长
    private final long expireTime = 24*60*60*1000;

    //编码密钥
    private final String signKey = "123456";

    /**
     * 使用jwt根据用户名生成token
     * @param username
     * @return
     */
    public String createToken(String username) {
        String token = Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS512, signKey).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    /**
     * 根据token字符串得到用户信息
     * @param token
     * @return
     */
    public String getUserInfoFromToken(String token) {
        String userInfo = Jwts.parser().setSigningKey(signKey).parseClaimsJws(token).getBody().getSubject();
        return userInfo;
    }

    /**
     * 删除token
     * @param token
     */
    public void removeToken(String token) {
        //将redis缓存中保存的token删除即可
    }
}
