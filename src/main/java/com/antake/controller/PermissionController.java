package com.antake.controller;


import com.antake.annotations.EnableLog;
import com.antake.pojo.Permission;
import com.antake.service.PermissionService;
import com.antake.utils.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/permission")
@Api(tags = "权限接口")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "获取权限")
    @EnableLog(value = "获取权限")
    @GetMapping
    @RequiresPermissions("system:permission:select")
    public DataResult<List<Permission>> listPermissions(){
        return permissionService.listPermissions();
    }


    @ApiOperation(value = "通过角色id拿到对应的权限")
    @RequiresPermissions("system:permission:select")
    @EnableLog(value = "通过角色id拿到对应的权限")
    @GetMapping("/{roleId}")
    public DataResult findPermissionsByRoleId(@PathVariable("roleId")Integer roleId){
        return permissionService.findMenusByRoleId(roleId);
    }
}

