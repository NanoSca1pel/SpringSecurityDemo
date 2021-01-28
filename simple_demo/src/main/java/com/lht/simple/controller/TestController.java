package com.lht.simple.controller;

import com.lht.simple.entity.MyUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhtao
 * @date 2021/1/11 13:37
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/auth")
    public String auth() {
        return "auth";
    }

    @GetMapping("/anyAuth")
    public String anyAuth() {
        return "anyAuth";
    }

    @GetMapping("/role")
    public String role() {
        return "role";
    }

    @GetMapping("/anyRole")
    public String anyRole() {
        return "anyRole";
    }

    @GetMapping("/secured")
    @Secured({"ROLE_sale","ROLE_manager"}) //校验角色时只需有其中一个角色即可访问
    public String secured() {
        return "secured";
    }

    @GetMapping("/preAuthorize")
    @PreAuthorize("hasAnyAuthority('admin')") //hasAuthority/hasAnyAuthority/hasRole/hasAnyRole都可以使用，在进入接口前调用
    public String preAuthorize() {
        System.out.println("preAuthorize");
        return "preAuthorize";
    }

    @GetMapping("/postAuthorize")
    @PostAuthorize("hasAnyAuthority('admin')") //hasAuthority/hasAnyAuthority/hasRole/hasAnyRole都可以使用，在接口调用完成后return使用
    public String postAuthorize() {
        System.out.println("postAuthorize");
        return "postAuthorize";
    }

    @GetMapping("/postFilter")
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostFilter("filterObject == 'a'") //权限校验之后对集合类型的返回数据进行过滤，只返回符合要求的数据。表达式中的filterObject引用表示集合中的某一元素
    public List<String> postFilter() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        return list;
    }

    @PostMapping("/preFilter")
    @PreAuthorize("hasAnyAuthority('admin')")
    @PreFilter(filterTarget= "user", value = "filterObject.id == 1") //对集合类型传入参数进行过滤，只有符合条件的入参才能请求接口。表达式中的filterObject引用表示集合中的某一元素。当@PreFilter标注的方法拥有多个集合类型的参数时，需要通过@PreFilter的filterTarget属性指定当前@PreFilter是针对哪个参数进行过滤的。
    public String postFilter(@RequestBody List<MyUser> user) {
        return user.toString();
    }

    public static void main(String[] args) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456a");
        System.out.println(encode);
        //String encode = "$2a$10$NBPbtbY4/oy41Mvp243HnO7C727vRzOv3nmgl4u5qgmHhaTRgZXNK";
        //System.out.println(encode);
        //String old = "123456";
        //System.out.println(passwordEncoder.matches(old, encode));
    }
}