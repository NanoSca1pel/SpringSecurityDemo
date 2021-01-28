package com.lht.simple.config;

import com.lht.simple.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 使用自定义实现类的方式设置账号密码
 * @author lhtao
 * @date 2021/1/11 14:07
 */
@Configuration
public class SecurityConfig2 extends WebSecurityConfigurerAdapter {

    @Resource
    private MyUserDetailsService myUserDetailsService;

    //注入数据源
    @Resource
    private DataSource dataSource;

    //配置rememberMe需要的jdbc操作对象
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //可以自动创建rememberMe需要的表，现在已经手动创建
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * access(String) 如果给定的SpEL表达式计算结果为true，就允许访问
     * anonymous() 允许匿名用户访问
     * authenticated() 允许认证的用户进行访问
     * denyAll() 无条件拒绝所有访问
     * fullyAuthenticated() 如果用户是完整认证的话（不是通过Remember-me功能认证的），就允许访问
     * hasAuthority(String) 如果用户具备给定权限的话就允许访问
     * hasAnyAuthority(String…)如果用户具备给定权限中的某一个的话，就允许访问
     * hasRole(String) 如果用户具备给定角色(用户组)的话,就允许访问/
     * hasAnyRole(String…) 如果用户具有给定角色(用户组)中的一个的话,允许访问.
     * hasIpAddress(String 如果请求来自给定ip地址的话,就允许访问.
     * not() 对其他访问结果求反.
     * permitAll() 无条件允许访问
     * rememberMe() 如果用户是通过Remember-me功能认证的，就允许访问
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() //自定义编写登录界面
                .loginPage("/login.html") //登录页面设置
                .loginProcessingUrl("/user/login") //登录访问接口路径
                .defaultSuccessUrl("/success.html") //登录成功后需要跳转的接口路径或网页路径（所有Url结尾的可以是接口地址，也可以是网页地址）
                .permitAll()  //以上关于formLogin的所有配置无条件允许访问
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/test/hello").permitAll() //设置退出接口与退出成功后跳转的接口
                .and() //添加其他配置
                .exceptionHandling().accessDeniedPage("/unauth.html") //配置没有访问权限访问跳转自定义页面
                .and()
                .authorizeRequests() //认证请求路径
                .antMatchers("/","/test/hello","/user/login")//设置哪些路径可以直接访问，不需要认证
                .permitAll()  //以上关于authorizeRequests的所有配置无条件允许访问
                .antMatchers("/test/auth").hasAuthority("admin") //当登录用户具有admin权限才可以访问这个路径
                .antMatchers("/test/anyAuth").hasAnyAuthority("admin,test") //当登录用户具有admin或者test中任一权限才可以访问这个路径
                .antMatchers("/test/role").hasRole("dev") //当登录用户具有admin角色才可以访问这个路径
                .antMatchers("/test/anyRole").hasAnyRole("admin,manager,test") //当登录用户具有admin、manager、test中任一角色才可以访问这个路径
                .anyRequest().authenticated() //当通过认证后所有请求都可以访问
                .and().rememberMe().tokenRepository(persistentTokenRepository())  //配置rememberMe需要的数据库操作对象
                .tokenValiditySeconds(60) //设置token有效时间，单位秒
                .userDetailsService(myUserDetailsService) //配置用户操作类
                .and().csrf().disable();  //关闭csrf防护


    }
}
