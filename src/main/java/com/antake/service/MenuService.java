package com.antake.service;

import com.antake.pojo.Menu;
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
 * @since 2020-05-16
 */
public interface MenuService extends IService<Menu> {
    /**
    * @Description: 通过userId拿到数组
    * @Param: [userId]
    * @return: com.antake.utils.DataResult<java.util.List<com.antake.pojo.Permission>>
    * @Author: MiZiMi
    * @Date: 2020/5/16 18:45
    */
    DataResult<List<Permission>> listMenusByUserId(String userId);
    /**
    * @Description: 获取所有的菜单
    * @Param: []
    * @return: com.antake.utils.DataResult<java.util.List<com.antake.pojo.Menu>>
    * @Author: MiZiMi
    * @Date: 2020/5/16 21:56
    */
    DataResult<List<Menu>> listMenus();

    DataResult deleteMenuByMenuId(Integer id);

    DataResult saveMenu(Menu menu);

    DataResult updateMenu(Menu menu);

    DataResult findMenusByRoleId(Integer roleId);
}
