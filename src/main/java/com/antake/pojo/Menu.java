package com.antake.pojo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author antake
 * @since 2020-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Menu对象", description="")
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "路由不能为空")
    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "是否是外链")
    private Boolean iFrame;

    @NotBlank(message = "菜单名称不能为空")
    @ApiModelProperty(value = "菜单名称")

    private String name;
    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题",hidden = true)
    private String title;

    @NotBlank(message = "组件不能为空")
    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "角色",hidden = true)
    private String role;
    @JsonIgnore
    public String getRole() {
        return role;
    }
    @JsonProperty
    public void setRole(String role) {
        this.role = role;
    }

    @ApiModelProperty(value = "上级菜单")
    private Integer pid;

    @ApiModelProperty(value = "排序",hidden = true)
    private Integer sort;
    @JsonIgnore
    public String getIcon() {
        return icon;
    }
    @JsonProperty
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @ApiModelProperty(value = "图标",hidden = true)
    private String icon;

    @TableField(exist = false)
    @ApiModelProperty(value = "meta")
    private JSONObject meta;
    @JsonIgnore
    public Boolean getNoCache() {
        return noCache;
    }
    @JsonProperty
    public void setNoCache(Boolean noCache) {
        this.noCache = noCache;
    }

    @ApiModelProperty(value = "是否缓存",hidden = true)
    private Boolean noCache;

    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

    @ApiModelProperty(value = "当设置 noRedirect 的时候该路由在面包屑导航中不可被点击")
    private String redirect;

    @ApiModelProperty(value = "设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由")
    private Boolean alwaysShow;
    @JsonIgnore
    public Boolean getBreadcrumb() {
        return breadcrumb;
    }
    @JsonProperty
    public void setBreadcrumb(Boolean breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    @ApiModelProperty(value = "如果设置为false，则不会在breadcrumb面包屑中显示(默认 true)",hidden = true)
    private Boolean breadcrumb;
    @JsonIgnore
    public Boolean getAffix() {
        return affix;
    }
    @JsonProperty
    public void setAffix(Boolean affix) {
        this.affix = affix;
    }

    @ApiModelProperty(value = "若果设置为true，它则会固定在tags-view中(默认 false)",hidden = true)
    private Boolean affix;

    @JsonIgnore
    public String getActiveMenu() {
        return activeMenu;
    }
    @JsonProperty
    public void setActiveMenu(String activeMenu) {
        this.activeMenu = activeMenu;
    }

    @ApiModelProperty(value = "你想在侧边栏高亮文章列表的路由，就可以进行如下设置",hidden = true)
    private String activeMenu;

    //这么明明数据库已经通过角色拿到了角色对应的菜单，为什么还要加上角色信息呢
    //提供两种方式，一种直接从数据库拉取对应的菜单，第二种 拉取全部菜单在前端过滤角色
    @TableField(exist = false)
    @ApiModelProperty(value = "子菜单")
    private List<Menu> children;
    @JsonIgnore
    public String getTitle() {
        return title;
    }
    @JsonProperty
    public void setTitle(String title) {
        this.title = title;
    }
}
