package com.lht.simple.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lht.simple.entity.MyUser;
import com.lht.simple.mapper.MyUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 使用自定义实现类设置帐号密码
 * @author lhtao
 * @date 2021/1/11 14:45
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserMapper myUserMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //调用myUserMapper查询数据库
        LambdaQueryWrapper<MyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MyUser::getUsername, s);
        MyUser user = myUserMapper.selectOne(wrapper);

        if (user == null) {
            throw new UsernameNotFoundException("未找到用户");
        }

        //当添加角色时，需要在设定角色前加 ROLE_ 前缀，因为源码判断时根据前缀判断为角色
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_manager");
        //authorities.forEach(System.out::println);
        //return new User(user.getUsername(), new BCryptPasswordEncoder().encode(user.getPassword()), authorities);
        return new User(user.getUsername(), user.getPassword(), authorities);
    }


}
