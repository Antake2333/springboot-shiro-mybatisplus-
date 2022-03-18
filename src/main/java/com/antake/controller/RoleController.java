package com.antake.controller;


import com.antake.annotations.EnableLog;
import com.antake.pojo.Role;
import com.antake.service.RoleService;
import com.antake.utils.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色接口")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @RequiresPermissions("system:role:select")
    @GetMapping("/list")
    @EnableLog(value = "查询所有角色")
    @ApiOperation(value = "查询所有角色")
    public DataResult<List<Role>> listRoles(){
        return roleService.listRoles();
    }

    @RequiresPermissions("system:role:add")
    @ApiOperation(value = "增加角色")
    @PutMapping("/add")
    @EnableLog(value = "增加角色")
    public DataResult add(@RequestBody @Valid Role role){
        return roleService.add(role);
    }

    @RequiresPermissions("system:role:select")
    @ApiOperation(value = "通过角色名查询角色是否存在")
    @EnableLog(value = "通过角色名查询角色是否存在")
    @GetMapping("/checkRole/{name}")
    public DataResult<Integer> checkRole(@PathVariable("name")String name){
        return DataResult.getSuccessResult(roleService.checkRoleName(name));
    }

    @EnableLog(value = "更新角色信息")
    @RequiresPermissions("system:role:update")
    @ApiOperation(value = "更新角色信息")
    @PostMapping("/update")
    public DataResult update(@RequestBody @Valid Role role){
        return roleService.updateRole(role);
    }

    @EnableLog(value = "删除角色")
    @RequiresPermissions("system:role:delete")
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/delete/{id}")
    public DataResult delete(@PathVariable("id")Integer id){
        return roleService.delete(id);
    }

    @EnableLog(value = "修改角色菜单")
    @RequiresPermissions("system:roleMenu:update")
    @ApiOperation(value = "修改角色菜单")
    @PostMapping("/menu/{roleId}")
    public DataResult updateRoleMenu(@PathVariable("roleId")Integer roleId,@RequestBody List<Integer> menuIds){
        return roleService.updateRoleMenu(roleId,menuIds);
    }

    @EnableLog(value = "检查角色名是否存在")
    @RequiresPermissions("system:role:select")
    @ApiOperation(value = "检查角色名")
    @GetMapping("/checkRolename/{roleName}")
    public DataResult checkRolename(@PathVariable("roleName")String roleName){
        return DataResult.getSuccessResult(roleService.checkRoleName(roleName));
    }

    @EnableLog(value = "批量修改角色菜单")
    @RequiresPermissions("system:roleMenu:update")
    @ApiOperation(value = "批量修改角色菜单")
    @PostMapping("/menu")
    public DataResult batchUpdateRoleMenu(@RequestBody Map<String,Object> params){
        List<Integer> roleIds = (List<Integer>) params.get("roleIds");
        List<Integer> menuIds = (List<Integer>) params.get("menuIds");
        if (roleIds!=null && roleIds.size()>0){
            return roleService.batchUpdateRoleMenu(roleIds,menuIds);
        }
        return DataResult.getFailResult();
    }

    @EnableLog(value = "修改角色权限")
    @RequiresPermissions("system:rolePermission:update")
    @ApiOperation(value = "修改角色权限")
    @PostMapping("/permission/{roleId}")
    public DataResult updateRolePermission(@PathVariable("roleId")Integer roleId,@RequestBody List<Integer> permissionIds){
        return roleService.updateRolePermission(roleId,permissionIds);
    }

    @EnableLog(value = "批量修改角色权限")
    @RequiresPermissions("system:rolePermission:update")
    @ApiOperation(value = "批量修改角色权限")
    @PostMapping("/permission")
    public DataResult batchUpdateRolePermission(@RequestBody Map<String,Object> params){
        List<Integer> roleIds = (List<Integer>) params.get("roleIds");
        List<Integer> permissionIds = (List<Integer>) params.get("permissionIds");
        if (roleIds!=null && roleIds.size()>0){
            return roleService.batchUpdateRolePermission(roleIds,permissionIds);
        }
        return DataResult.getFailResult();
    }
}

