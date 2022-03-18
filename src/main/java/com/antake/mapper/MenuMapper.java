package com.antake.mapper;

import com.antake.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author antake
 * @since 2020-05-16
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> listMenusByUserId(@Param("userId") String userId);

    List<Menu> listMenus();

    List<Integer> listMenuIds(@Param("userId") String userId);
}
