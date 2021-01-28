package com.lht.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lht.simple.entity.MyUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lhtao
 * @date 2021/1/11 15:20
 */
@Mapper
public interface MyUserMapper extends BaseMapper<MyUser> {
}
