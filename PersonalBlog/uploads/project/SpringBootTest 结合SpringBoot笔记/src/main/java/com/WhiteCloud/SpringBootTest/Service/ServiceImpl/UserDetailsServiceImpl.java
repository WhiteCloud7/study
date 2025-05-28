//package com.WhiteCloud.SpringBootTest.Service.ServiceImpl;
//
//import com.WhiteCloud.SpringBootTest.Entity.userInfo;
//import com.WhiteCloud.SpringBootTest.Service.jpaUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//    public static String remindMessage;
//
//    private jpaUserService jpaUserService;
//    @Autowired
//    public UserDetailsServiceImpl(jpaUserService jpaUserService) {
//        this.jpaUserService = jpaUserService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        //通过用户名得到用户信息
//        userInfo userInfo = jpaUserService.findByUsername(username);
//        if(userInfo!=null){
//            //获取角色
//            String role = userInfo.getRole().getRoleName();
//            //角色集合
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            //角色必须加ROLE_，数据库没有就代码加
//            authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
//            //这里User时spring security里面的
//            return new User(userInfo.getUsername(),userInfo.getPassword(),authorities);
//        }else{
//            remindMessage = "用户不存在！";
//            throw new UsernameNotFoundException(remindMessage);
//        }
//    }
//}
