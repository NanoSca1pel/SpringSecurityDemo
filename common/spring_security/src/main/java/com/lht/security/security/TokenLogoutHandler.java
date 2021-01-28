package com.lht.security.security;

import com.lht.utils.utils.R;
import com.lht.utils.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出处理器
 * @author lhtao
 * @date 2021/1/27 9:01
 */
public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        //1. 从header里面获取token
        //2. token不为空，移除token，从redis删除token
        String token = request.getHeader("token");
        if (token != null) {
            //移除
            tokenManager.removeToken(token);
            //获取用户名称
            String username = tokenManager.getUserInfoFromToken(token);
            //从redis中删除
            redisTemplate.delete(username);
        }
        ResponseUtil.out(response, R.ok());
    }
}
