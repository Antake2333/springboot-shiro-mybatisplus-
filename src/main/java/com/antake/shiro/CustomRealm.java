package com.antake.shiro;

import com.alibaba.druid.util.StringUtils;
import com.antake.constants.Constant;
import com.antake.pojo.Permission;
import com.antake.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Antake
 * @date 2020/5/8
 * @description this is description
 */
public class CustomRealm extends AuthorizingRealm {
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CustomUsernamePasswordToken;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String accessToken= (String) principals.getPrimaryPrincipal();
        Claims claimsFromToken = JwtUtil.getClaimsFromToken(accessToken);
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        if (claimsFromToken.get(Constant.JWT_USER_PERMISSIONS)!=null){
            //遍历权限拿到所有有permission的
            List<LinkedHashMap> permissionList= (List<LinkedHashMap>) claimsFromToken.get(Constant.JWT_USER_PERMISSIONS);
            List<String> list=new ArrayList<>();
            for (LinkedHashMap permission : permissionList) {
                Object perm = permission.get("permission");
                if (!StringUtils.isEmpty(perm.toString())){
                    list.add(perm.toString());
                }
            }
            info.addStringPermissions(list);
        }
        if (claimsFromToken.get(Constant.JWT_USER_ROLES)!=null){
            info.addRoles((Collection<String>) claimsFromToken.get(Constant.JWT_USER_ROLES));
        }
        return info;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomUsernamePasswordToken customUsernamePasswordToken=(CustomUsernamePasswordToken)authenticationToken;
        return new SimpleAuthenticationInfo(customUsernamePasswordToken.getPrincipal(),customUsernamePasswordToken.getCredentials(),this.getName());
    }
}

