package com.example.demo.resource;

import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.service.ILoginService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private ILoginService loginService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
      //获取登录用户名
        String name = (String) principals.getPrimaryPrincipal();
        //查询用户名称
        User user  = loginService.findByName(name);
        //添加角色和权限
        SimpleAuthorizationInfo  simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : user.getRoles()) {
            simpleAuthorizationInfo.addRole(role.getRoleName());
            for (Permission permission : role.getPermissions()) {
                simpleAuthorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return simpleAuthorizationInfo;
    }
//用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
     //加这一步的目的是在post 请求的时候会先进认证，然后在到请求
      if(token.getPrincipal() == null) {
          return null;
      }
      //获取用户信息
        String  name = token.getPrincipal().toString();
      User user = loginService.findByName(name);
      if (user == null) {
          //这里返回后会报出对应异常
          return  null;
      }else {
          //这里验证token
          SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name,user.getPassword().toString(),getName());
          return simpleAuthenticationInfo;
      }

    }
}
