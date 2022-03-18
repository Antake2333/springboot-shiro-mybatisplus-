package com.antake.mapper;

import com.antake.pojo.User;
import com.antake.vo.JwtUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
public interface UserMapper extends BaseMapper<User> {
    /**
    * @Description: 通过用户名获取用户信息和权限信息
    * @Param: [username]
    * @return: com.antake.vo.JwtUser
    * @Author: MiZiMi
    * @Date: 2020/5/11 16:48
    */
    JwtUser getUserInfoByUsername(@Param("username")String username);
}
