package com.antake.controller;
import com.antake.annotations.EnableLog;
import com.antake.pojo.User;
import com.antake.service.UserService;
import com.antake.utils.DataResult;
import com.antake.utils.JwtUtil;
import com.antake.vo.JwtUser;
import com.antake.vo.req.LoginVO;
import com.antake.vo.req.PageVO;
import com.antake.vo.resp.LoginRespVO;
import com.antake.vo.resp.PageRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public DataResult<LoginRespVO> login(@RequestBody @Valid LoginVO loginVO){
        return userService.login(loginVO);
    }
    @RequiresPermissions("system:user:select")
    @GetMapping("/list")
    @EnableLog(value = "分页获取所有用户")
    @ApiOperation(value = "获取所有的用户")
    public DataResult<PageRespVO<User>> listUser(@Valid PageVO pageVO){
        return userService.listUser(pageVO);
    }
    @EnableLog(value = "获取用户")
    @RequiresPermissions("system:user:select")
    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息")
    public DataResult<JwtUser> getUserInfo(){
        return userService.getUserInfoByUsername(JwtUtil.getUsername(JwtUtil.getTokenFromRequest()));
    }
    @EnableLog(value = "获取用户信息")
    @RequiresPermissions("system:user:select")
    @GetMapping("/{id}")
    @ApiOperation(value = "获取用户信息")
    public DataResult<User> getUserByUserId(@PathVariable("id")Long id){
        return userService.getUserInfoByUserId(id);
    }
    @EnableLog(value = "退出登录")
    @ApiOperation(value = "登出")
    @DeleteMapping("/logout")
    public DataResult logout(){
       userService.logout(JwtUtil.getTokenFromRequest());
       return DataResult.getSuccessResult();
    }
    @EnableLog(value = "禁用用户")
    @RequiresPermissions("system:user:ban")
    @ApiOperation(value = "禁用用户")
    @GetMapping("/changeStatus/{id}")
    public DataResult changeStatus(@PathVariable("id")Long id,@RequestParam("status")Integer status){
        return userService.changeStatus(id,status);
    }
    @EnableLog(value = "删除用户")
    @RequiresPermissions("system:user:delete")
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/delete/{id}")
    public DataResult delete(@PathVariable("id")Long id){
        return userService.delete(id);
    }
    @RequiresPermissions("system:user:add")
    @EnableLog(value = "增加用户")
    @ApiOperation(value = "增加用户")
    @PutMapping("/add")
    public DataResult add(@RequestBody @Valid User user){
        return userService.add(user);
    }
    @RequiresPermissions("system:user:update")
    @EnableLog(value = "修改用户信息")
    @ApiOperation(value = "修改用户信息")
    @PostMapping("/update")
    public DataResult update(@RequestBody @Valid User user){
        return userService.updateUser(user);
    }
    @EnableLog(value = "检查用户名是否存在")
    @RequiresPermissions("system:user:select")
    @ApiOperation(value = "检查用户名是否存在")
    @GetMapping("/checkUsername/{username}")
    public DataResult checkUsername(@PathVariable("username")String username){
        return DataResult.getSuccessResult(userService.checkUserByUsername(username));
    }

    @EnableLog(value = "踢人下线")
    @RequiresPermissions("monitor:onlineUser:ban")
    @ApiOperation(value = "踢人下线")
    @DeleteMapping("/ban")
    public DataResult ban(@RequestBody Set<Long> userIds){
        for (Long userId : userIds) {
            userService.kickOutUserById(userId);
        }
        return DataResult.getSuccessResult();
    }

    @EnableLog(value = "分页获取在线用户")
    @RequiresPermissions("monitor:onlineUser:select")
    @ApiOperation(value = "获取在线用户")
    @GetMapping("/online")
    public DataResult listOnlineUser(@Valid PageVO pageVO){
        return userService.listOnlineUser(pageVO);
    }

    @EnableLog(value = "用户分配角色")
    @RequiresPermissions("system:user:role:update")
    @ApiOperation(value = "用户分配角色")
    @PostMapping("/role/update/{userId}")
    public DataResult updateUserRole(@PathVariable("userId") Long userId,@RequestBody List<Integer> roleIds){
        return userService.updateUserRole(userId,roleIds);
    }
    @EnableLog(value = "通过用户id查询用户角色")
    @RequiresPermissions("system:user:role:select")
    @ApiOperation(value = "通过用户id查询用户角色")
    @GetMapping("/role/{userId}")
    public DataResult getUserRoleByUserId(@PathVariable("userId") Long userId){
        return userService.getUserRoleByUserId(userId);
    }
}

