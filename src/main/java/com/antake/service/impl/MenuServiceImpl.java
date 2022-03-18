package com.antake.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.antake.mapper.RoleMenuMapper;
import com.antake.pojo.Menu;
import com.antake.mapper.MenuMapper;
import com.antake.pojo.Permission;
import com.antake.pojo.RoleMenu;
import com.antake.service.MenuService;
import com.antake.utils.DataResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author antake
 * @since 2020-05-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional(readOnly = true)
    public DataResult<List<Permission>> listMenusByUserId(String userId) {
        //通过角色拿到 menuIds
        List<Integer> menuIds=menuMapper.listMenuIds(userId);
        List<Menu> menus = menuMapper.listMenusByUserId(userId);
        //通过流过滤
        List<Menu> menuList = filterList(menus, menuIds);
        //拿到了menus之后进行封装
        buildMenus(menuList);
        return DataResult.getSuccessResult(menuList);
    }
    private List<Menu> filterList(List<Menu> list,List<Integer> menuIds){
        List<Menu> menus = new ArrayList<>();
        if (list!=null && list.size()>0){
            menus = list.stream().filter(menu -> menuIds.contains(menu.getId())).collect(Collectors.toList());
            for (Menu menu : list) {
                if (menu.getChildren()!=null && menu.getChildren().size()>0){
                    menu.setChildren(filterList(menu.getChildren(),menuIds));
                }
            }
        }
        return menus;
    }
    @Override
    @Transactional(readOnly = true)
    public DataResult<List<Menu>> listMenus() {
        List<Menu> menus = menuMapper.listMenus();
        //拿到了menus之后进行封装
        buildMenus(menus);
        return DataResult.getSuccessResult(menus);
    }

    @Override
    public DataResult deleteMenuByMenuId(Integer id) {
        if(id==1){
            return DataResult.getFailResult("对不起，无论是谁都不能删除系统管理菜单哦");
        }
        //递归拿到所有子菜单的id
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(id);
        getIds(ids,id);
        int result = menuMapper.deleteBatchIds(ids);
        if (result > 0) {
            //删除Role menu中的
            roleMenuMapper.delete(new UpdateWrapper<RoleMenu>().in("menu_id",ids));
            return DataResult.getSuccessResult();
        }
        return DataResult.getFailResult();
    }

    @Override
    public DataResult saveMenu(Menu menu) {
        menu.setId(null);
        int result = menuMapper.insert(menu);
        if (result > 0) {
            return DataResult.getSuccessResult();
        } else {
            return DataResult.getFailResult();
        }
    }

    @Override
    public DataResult updateMenu(Menu menu) {
        int result = menuMapper.updateById(menu);
        if (result > 0) {
            return DataResult.getSuccessResult();
        } else {
            return DataResult.getFailResult();
        }
    }

    @Override
    public DataResult findMenusByRoleId(Integer roleId) {
        List<Integer> menuIds = roleMenuMapper.selectList(new QueryWrapper<RoleMenu>().eq("role_id", roleId)).stream().map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());
        return DataResult.getSuccessResult(menuIds);
    }

    /**
     * @Description: 根据前端的需要构建 menu树
     * @Param: [originList]
     * @return: void
     * @Author: MiZiMi
     * @Date: 2020/5/16 21:58
     */
    private void buildMenus(List<Menu> originList) {
        for (Menu menu : originList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", menu.getTitle());
            jsonObject.put("icon", menu.getIcon());
            jsonObject.put("noCache", menu.getNoCache());
            jsonObject.put("breadcrumb", menu.getBreadcrumb());
            jsonObject.put("affix", menu.getAffix());
            jsonObject.put("activeMenu", menu.getActiveMenu());
            if (menu.getRole() != null && !StringUtils.isEmpty(menu.getRole())) {
                jsonObject.put("roles", Arrays.asList(menu.getRole().split(",")));
            }
            menu.setMeta(jsonObject);
            if (menu.getChildren() != null && menu.getChildren().size() > 0) {
                buildMenus(menu.getChildren());
            }
        }
    }
    private void getIds(List<Integer> ids,Integer id){
        //查询二级分类的对象
        QueryWrapper<Menu> Wrapper = new QueryWrapper<>();
        Wrapper.eq("pid",id);
        List<Menu> menuList = menuMapper.selectList(Wrapper);
        //遍历二级分类的对象，把二级分类的id加入到要删除的集合中
        for (Menu menu : menuList) {
            ids.add(menu.getId());
            if (menu.getChildren()!=null && menu.getChildren().size()>0){
                this.getIds(ids,menu.getId());
            }
        }
    }
}
