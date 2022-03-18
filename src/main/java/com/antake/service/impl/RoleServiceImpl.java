package com.antake.service.impl;

import com.antake.mapper.RoleMenuMapper;
import com.antake.mapper.RolePermissionMapper;
import com.antake.mapper.UserRoleMapper;
import com.antake.pojo.Role;
import com.antake.mapper.RoleMapper;
import com.antake.pojo.RoleMenu;
import com.antake.pojo.RolePermission;
import com.antake.pojo.UserRole;
import com.antake.service.RoleMenuService;
import com.antake.service.RolePermissionService;
import com.antake.service.RoleService;
import com.antake.utils.DataResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    @Transactional(readOnly = true)
    public DataResult<List<Role>> listRoles() {
        return DataResult.getSuccessResult(list());
    }

    @Override
    public DataResult add(Role role) {
        //先检查名字存不存在
        long count = checkRoleName(role.getName());
        if (count>0){
            return DataResult.getFailResult("角色名已存在");
        }
        //增加到数据库
        role.setId(null);
        int insert = roleMapper.insert(role);
        if (insert>0){
            return DataResult.getSuccessResult();
        }else {
            return DataResult.getFailResult();
        }
    }
    @Override
    @Transactional(readOnly = true)
    public Long checkRoleName(String roleName){
       return  roleMapper.selectCount(new QueryWrapper<Role>().eq("name",roleName));
    }

    @Override
    public DataResult updateRole(Role role) {
        Role oldRole = roleMapper.selectOne(new QueryWrapper<Role>().eq("id", role.getId()));
        if (!oldRole.getName().equals(role.getName())){
            long count = checkRoleName(role.getName());
            if (count>0){
                return DataResult.getFailResult("角色名称已存在");
            }
        }
        int result = roleMapper.updateById(role);
        if (result>0){
            return DataResult.getSuccessResult();
        }else {
            return DataResult.getFailResult();
        }
    }

    @Override
    public DataResult delete(Integer id) {
        if (id==1){
            return DataResult.getFailResult("对不起，无论是谁都无权删除超级管理员");
        }
        //先检查这个存在吗
        long count = roleMapper.selectCount(new QueryWrapper<Role>().eq("id", id));
        if (count>0){
            //数量不为空再去删除
            //删除角色id之前要删除其他用到该角色的表
            //user_role
            userRoleMapper.delete(new UpdateWrapper<UserRole>().eq("role_id",id));
            //role_permission
            rolePermissionMapper.delete(new UpdateWrapper<RolePermission>().eq("role_id",id));
            //role_menu
            roleMenuMapper.delete(new UpdateWrapper<RoleMenu>().eq("role_id",id));
            //role
            int result = roleMapper.deleteById(id);
            if (result>0){
                return DataResult.getSuccessResult();
            }
        }
        return DataResult.getFailResult();
    }

    @Override
    public DataResult updateRoleMenu(Integer roleId, List<Integer> menuIds) {
        //首先删除所有对应的menu
        roleMenuMapper.delete(new UpdateWrapper<RoleMenu>().eq("role_id", roleId));
        if (menuIds.size()>0){
            ArrayList<RoleMenu> roleMenus = new ArrayList<>(menuIds.size());
            for (int i = 0; i < menuIds.size(); i++) {
                roleMenus.add(new RoleMenu().setRoleId(roleId).setMenuId(menuIds.get(i)));
            }
            boolean result = roleMenuService.saveBatch(roleMenus);
            if (result){
                return DataResult.getSuccessResult();
            }else {
                return DataResult.getFailResult();
            }
        }
        return DataResult.getSuccessResult();
    }

    @Override
    public DataResult batchUpdateRoleMenu(List<Integer> roleIds, List<Integer> menuIds) {
        for (Integer roleId : roleIds) {
            //首先删除所有对应的menu
            roleMenuMapper.delete(new UpdateWrapper<RoleMenu>().eq("role_id", roleId));
            if (menuIds!=null && menuIds.size()>0){
                ArrayList<RoleMenu> roleMenus = new ArrayList<>(menuIds.size());
                for (int i = 0; i < menuIds.size(); i++) {
                    roleMenus.add(new RoleMenu().setRoleId(roleId).setMenuId(menuIds.get(i)));
                }
                boolean result = roleMenuService.saveBatch(roleMenus);
                if (!result){
                    return DataResult.getFailResult();
                }
            }
        }
        return DataResult.getSuccessResult();
    }

    @Override
    public DataResult updateRolePermission(Integer roleId, List<Integer> permissionIds) {
        //首先删除所有对应的permission
        rolePermissionMapper.delete(new UpdateWrapper<RolePermission>().eq("role_id", roleId));
        if (permissionIds.size()>0){
            ArrayList<RolePermission> rolePermissions = new ArrayList<>(permissionIds.size());
            for (int i = 0; i < permissionIds.size(); i++) {
                rolePermissions.add(new RolePermission().setRoleId(roleId).setPermissionId(permissionIds.get(i)));
            }
            boolean result = rolePermissionService.saveBatch(rolePermissions);
            if (result){
                return DataResult.getSuccessResult();
            }else {
                return DataResult.getFailResult();
            }
        }
        return DataResult.getSuccessResult();
    }

    @Override
    public DataResult batchUpdateRolePermission(List<Integer> roleIds, List<Integer> permissionIds) {
        for (Integer roleId : roleIds) {
            //首先删除所有对应的permission
            roleMenuMapper.delete(new UpdateWrapper<RoleMenu>().eq("role_id", roleId));
            if (permissionIds!=null && permissionIds.size()>0){
                ArrayList<RolePermission> rolePermissions = new ArrayList<>(permissionIds.size());
                for (int i = 0; i < permissionIds.size(); i++) {
                    rolePermissions.add(new RolePermission().setRoleId(roleId).setPermissionId(permissionIds.get(i)));
                }
                boolean result = rolePermissionService.saveBatch(rolePermissions);
                if (!result){
                    return DataResult.getFailResult();
                }
            }
        }
        return DataResult.getSuccessResult();
    }
}
