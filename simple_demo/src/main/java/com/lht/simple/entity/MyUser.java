package com.lht.simple.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @author lhtao
 * @date 2021/1/11 15:17
 */
@Data
@Builder
@TableName("user")
public class MyUser {

    private Integer id;

    private String username;

    private String password;
}
