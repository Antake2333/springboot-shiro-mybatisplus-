package com.antake.service.impl;

import com.antake.mapper.RolePermissionMapper;
import com.antake.pojo.Permission;
import com.antake.mapper.PermissionMapper;
import com.antake.pojo.RoleMenu;
import com.antake.pojo.RolePermission;
import com.antake.service.PermissionService;
import com.antake.utils.DataResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    @Transactional(readOnly = true)
    public DataResult<List<Permission>> listPermissionsByUserIdAndTypeOrderByAsc(String userId,int type) {
        return DataResult.getSuccessResult(permissionMapper.listPermissionsByUserIdAndTypeOrderByAsc(userId,type));
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult<List<Permission>> listPermissions() {
        return DataResult.getSuccessResult(permissionMapper.listPermissions());
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult findMenusByRoleId(Integer roleId) {
        List<Integer> permissionIds = rolePermissionMapper.selectList(new QueryWrapper<RolePermission>().eq("role_id", roleId)).stream().map(rolePermission -> rolePermission.getPermissionId()).collect(Collectors.toList());
        return DataResult.getSuccessResult(permissionIds);
    }
}
