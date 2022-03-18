package com.antake.config;

import com.antake.shiro.*;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroCacheManager shiroCacheManager() {
        return new ShiroCacheManager();
    }

    //创建密码加密对象
    @Bean
    public CustomHashedCredentialsMatcher customHashedCredentialsMatcher() {
        CustomHashedCredentialsMatcher customHashedCredentialsMatcher = new CustomHashedCredentialsMatcher();
        return customHashedCredentialsMatcher;
    }

    //创建realm对象，需要自定义
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(customHashedCredentialsMatcher());
        customRealm.setCacheManager(shiroCacheManager());
        return customRealm;
    }

    //禁用session
    @Bean(name = "sessionManager")
    public DefaultSessionManager sessionManager() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        return sessionManager;
    }
    @Bean(name = "subjectFactory")
    public StatelessDefaultSubjectFactory statelessDefaultSubjectFactory() {
        return new StatelessDefaultSubjectFactory();
    }

    @Bean(name = "defaultSessionStorageEvaluator")
    public DefaultSessionStorageEvaluator defaultSessionStorageEvaluator() {
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        return defaultSessionStorageEvaluator;
    }

    @Bean(name = "subjectDAO")
    public DefaultSubjectDAO subjectDAO() {
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        defaultSubjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator());
        return defaultSubjectDAO;
    }

    //DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //禁用session
        /*defaultWebSecurityManager.setSubjectDAO(subjectDAO());
        defaultWebSecurityManager.setSubjectFactory(statelessDefaultSubjectFactory());
        defaultWebSecurityManager.setSessionManager(sessionManager());*/
        //设置rememberMe
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
        //推荐放到最后，不然某些情况可能不生效
        defaultWebSecurityManager.setRealm(customRealm());
        return defaultWebSecurityManager;
    }

    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(getDefaultWebSecurityManager());
        //设置自定义filter
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        //filterMap.put("roleOrFilter",new RolesOrAuthorizationFilter());
        filterMap.put("token", new CustomAccessControlFilter());
        bean.setFilters(filterMap);

        //添加Shiro的过滤器/拦截器 要用linkedhashmap
        Map<String, String> filterChainMap = new LinkedHashMap<>();
        //swagger
        filterChainMap.put("/swagger/**", "anon");
        filterChainMap.put("/v2/api-docs", "anon");
        filterChainMap.put("/doc.html", "anon");
        filterChainMap.put("/swagger-resources/**", "anon");
        filterChainMap.put("/webjars/**", "anon");
        filterChainMap.put("/favicon.ico", "anon");
        filterChainMap.put("/captcha.jpg", "anon");
        //druid sql监控配置
        filterChainMap.put("/druid/**", "anon");
        //登录
        filterChainMap.put("/user/login", "anon");
        filterChainMap.put("/auth/code", "anon");
        //放行登录菜单
        filterChainMap.put("/menu/business/build/**", "anon");
        //刷新token
        filterChainMap.put("/auth/refreshToken","anon");
        //登出
        filterChainMap.put("/user/logout", "anon");
        filterChainMap.put("/**", "token,authc");
        bean.setFilterChainDefinitionMap(filterChainMap);
        return bean;
    }

    //使权限控制注解生效
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(getDefaultWebSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }
    //rememberMe

    /**
     * cookie对象
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        // 设置cookie名称，对应login.html页面的<input type="checkbox" name="rememberMe"/>
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // 设置cookie的过期时间，单位为秒，这里为一天
        cookie.setMaxAge(86400);
        return cookie;
    }

    /**
     * cookie管理对象
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie加密的密钥
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }
}

