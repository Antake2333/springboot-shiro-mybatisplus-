package com.antake.service;

import com.antake.pojo.Role;
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
public interface RoleService extends IService<Role> {
    /**
    * @Description: 获取所有接口
    * @Param: []
    * @return: com.antake.utils.DataResult<java.util.List<com.antake.pojo.Role>>
    * @Author: MiZiMi
    * @Date: 2020/5/13 21:43
    */
    DataResult<List<Role>> listRoles();

    DataResult add(Role role);
    Long checkRoleName(String roleName);

    DataResult updateRole(Role role);

    DataResult delete(Integer id);

    DataResult updateRoleMenu(Integer roleId, List<Integer> menuIds);

    DataResult batchUpdateRoleMenu(List<Integer> roleIds, List<Integer> menuIds);

    DataResult updateRolePermission(Integer roleId, List<Integer> permissionIds);

    DataResult batchUpdateRolePermission(List<Integer> roleIds, List<Integer> permissionIds);
}
