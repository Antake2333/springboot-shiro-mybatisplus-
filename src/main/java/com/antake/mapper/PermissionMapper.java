package com.antake.mapper;

import com.antake.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    LinkedList<Permission> listPermissionsByUserIdAndTypeOrderByAsc(String userId,int type);

    List<Permission> listPermissions();
}
