package com.antake.service;

import com.antake.pojo.Permission;
import com.antake.utils.DataResult;
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
public interface PermissionService extends IService<Permission> {

    DataResult<List<Permission>> listPermissionsByUserIdAndTypeOrderByAsc(String userId,int type);

    DataResult<List<Permission>> listPermissions();

    DataResult findMenusByRoleId(Integer roleId);
}
