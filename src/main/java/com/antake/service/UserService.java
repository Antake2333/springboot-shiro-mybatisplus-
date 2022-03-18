package com.antake.service;

import com.antake.pojo.User;
import com.antake.utils.DataResult;
import com.antake.vo.JwtUser;
import com.antake.vo.req.LoginVO;
import com.antake.vo.req.PageVO;
import com.antake.vo.resp.LoginRespVO;
import com.antake.vo.resp.PageRespVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
public interface UserService extends IService<User> {
    /**
    * @Description: 用户登录
    * @Param: [loginVO]
    * @return: com.antake.utils.DataResult<com.antake.vo.resp.LoginRespVO>
    * @Author: MiZiMi
    * @Date: 2020/5/9 11:07
    */
    DataResult<LoginRespVO> login(LoginVO loginVO);
    /**
    * @Description: 获取所有的用户信息
    * @Param: []
    * @return: com.antake.utils.DataResult<java.util.List<com.antake.vo.UserVO>>
    * @Author: MiZiMi
    * @Date: 2020/5/9 18:41
    */
    DataResult<PageRespVO<User>> listUser(PageVO pageVO);
    /**
    * @Description: 通过用户名获取用户信息
    * @Param: [username]
    * @return: com.antake.utils.DataResult<com.antake.vo.JwtUser>
    * @Author: MiZiMi
    * @Date: 2020/5/11 11:00
    */
    DataResult<JwtUser> getUserInfoByUsername(String username);
    /**
    * @Description: 登出
    * @Param: []
    * @return: com.antake.utils.DataResult
    * @Author: MiZiMi
    * @Date: 2020/5/11 16:31
    */
    void logout(String token);
    /**
    * @Description: 通过用户Id禁用用户
    * @Param: [id]
    * @return: com.antake.utils.DataResult
    * @Author: MiZiMi
    * @Date: 2020/5/14 16:30
    */
    DataResult changeStatus(long id, int status);
    /**
    * @Description: 通过用户id删除用户
    * @Param: [id]
    * @return: com.antake.utils.DataResult
    * @Author: MiZiMi
    * @Date: 2020/5/14 17:11
    */
    DataResult delete(Long id);

    DataResult add(User user);

    DataResult updateUser(User user);

    Long checkUserByUsername(String username);
    /**
    * @Description: 强制用户下线
    * @Param: [id]
    * @return: void
    * @Author: MiZiMi
    * @Date: 2020/5/19 9:08
    */
    void kickOutUserById(Long id);
    /**
    * @Description: 获取在线用户
    * @Param: []
    * @return: com.antake.utils.DataResult
    * @Author: MiZiMi
    * @Date: 2020/5/19 9:41
     * @param pageVO
    */
    DataResult listOnlineUser(PageVO pageVO);
    /**
    * @Description: 通过用户id拿到用户信息
    * @Param: [id]
    * @return: com.antake.utils.DataResult<com.antake.vo.JwtUser>
    * @Author: MiZiMi
    * @Date: 2020/5/26 12:55
    */
    DataResult<User> getUserInfoByUserId(Long id);
    /**
    * @Description: 重新签发token
    * @Param: []
    * @return: com.antake.utils.DataResult
    * @Author: MiZiMi
    * @Date: 2020/5/28 20:13
    */
    DataResult refreshToken(String refreshToken,String loginType);
    /**
    * @Description: 更新用户角色
    * @Param: [userId, roleId]
    * @return: com.antake.utils.DataResult
    * @Author: MiZiMi
    * @Date: 2020/5/29 11:50
    */
    DataResult getUserRoleByUserId(Long userId);
    /**
    * @Description: 通过用户id更新用户角色
    * @Param: [userId, roleIds]
    * @return: com.antake.utils.DataResult
    * @Author: MiZiMi
    * @Date: 2020/5/29 23:37
    */
    DataResult updateUserRole(Long userId, List<Integer> roleIds);
}
