//package com.WhiteCloud.SpringBootTest.Model.ShiroRealm;
//
//import com.WhiteCloud.SpringBootTest.Entity.userInfo;
//import com.WhiteCloud.SpringBootTest.Service.userService;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.SimpleAuthenticationInfo;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.security.Principal;
//
//public class loginRealm extends AuthorizingRealm {
//    @Autowired
//    private userService userService;
//
//    public static StringBuffer remindMessage;
//
//    //用来登录前验证用户，获取认值信息
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        //用authenticationToken.getPrincipal获取用户名
//        String username = (String) authenticationToken.getPrincipal();
//        //有用户名判断是否有该用户
//        userInfo userInfo = userService.getUserInfoByUsername(username);
//        if(userInfo!=null){
//            //用SecurityUtils获取认证主体并将该用户存入session
//            SecurityUtils.getSubject().getSession().setAttribute(userInfo.getUsername(),userInfo);
//            //传入用户信息进行认证
//            AuthenticationInfo  authenticationInfo = new SimpleAuthenticationInfo(userInfo.getUsername(),userInfo.getPassword(),"loginRealm");
//            return authenticationInfo;
//        }else {
//            remindMessage.append("账号或密码错误！");
//            return null;
//        }
//    }
//    //用来登录后授予角色和权限
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){
//        //用principalCollection.getPrimaryPrincipal()得到用户名
//        String username = (String) principalCollection.getPrimaryPrincipal();
//        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        //给该用户设置角色和权限
//        authorizationInfo.setRoles(userService.getRoleNameByUsername(username));
//        authorizationInfo.setStringPermissions(userService.getPremissionInfoByUsername(username));
//        return authorizationInfo;
//    }
//}
