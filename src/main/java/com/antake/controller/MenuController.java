package com.antake.controller;

import com.antake.annotations.EnableLog;
import com.antake.pojo.Menu;
import com.antake.pojo.Permission;
import com.antake.service.MenuService;
import com.antake.utils.DataResult;
import com.antake.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Antake
 * @date 2020/5/11
 * @description this is description
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单接口")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @EnableLog(value = "获取左边菜单")
    @RequiresPermissions("system:menu:select")
    @ApiOperation(value = "获取左边菜单")
    @GetMapping("/build")
    public DataResult<List<Permission>> buildMenus() {
        return menuService.listMenusByUserId(JwtUtil.getUserId(JwtUtil.getTokenFromRequest()));
    }

    @EnableLog(value = "获取菜单")
    @ApiOperation(value = "获取菜单")
    @GetMapping
    @RequiresPermissions("system:menu:select")
    public DataResult<List<Menu>> listMenus() {
        return menuService.listMenus();
    }

    @EnableLog(value = "删除菜单")
    @RequiresPermissions("system:menu:delete")
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/delete/{id}")
    public DataResult deleteMenuByMenuId(@PathVariable("id") Integer id) {
        return menuService.deleteMenuByMenuId(id);
    }

    @EnableLog(value = "增加菜单")
    @RequiresPermissions("system:menu:add")
    @ApiOperation(value = "增加菜单")
    @PutMapping("/add")
    public DataResult add(@RequestBody @Valid Menu menu) {
        return menuService.saveMenu(menu);
    }

    @EnableLog(value = "修改菜单")
    @RequiresPermissions("system:menu:update")
    @ApiOperation(value = "修改菜单")
    @PostMapping("/update")
    public DataResult update(@RequestBody @Valid Menu menu) {
        return menuService.updateMenu(menu);
    }

    @EnableLog(value = "通过角色id拿到对应的菜单")
    @RequiresPermissions("system:menu:select")
    @ApiOperation(value = "通过角色id拿到对应的菜单")
    @GetMapping("/{roleId}")
    public DataResult findMenusByRoleId(@PathVariable("roleId") Integer roleId) {
        return menuService.findMenusByRoleId(roleId);
    }
}

