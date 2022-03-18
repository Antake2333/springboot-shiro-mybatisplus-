package com.antake.service.impl;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.antake.code.BaseResponseCodeEnum;
import com.antake.components.RedisUtil;
import com.antake.constants.Constant;
import com.antake.excepitions.BusinessException;
import com.antake.mapper.UserRoleMapper;
import com.antake.pojo.Log;
import com.antake.pojo.User;
import com.antake.mapper.UserMapper;
import com.antake.pojo.UserRole;
import com.antake.service.LogService;
import com.antake.service.UserRoleService;
import com.antake.service.UserService;
import com.antake.utils.DataResult;
import com.antake.utils.JwtUtil;
import com.antake.utils.PasswordUtils;
import com.antake.vo.JwtUser;
import com.antake.vo.OnlineUser;
import com.antake.vo.req.LoginVO;
import com.antake.vo.req.PageVO;
import com.antake.vo.resp.LoginRespVO;
import com.antake.vo.resp.PageRespVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private LogService logService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${rsa.privateKey}")
    private String rsaPrivateKey;
    @Value("${single.login}")
    private Boolean singleLogin;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Override
    public DataResult<LoginRespVO> login(LoginVO loginVO) {
        long startTime=System.currentTimeMillis();
        String code = (String) redisUtil.get(loginVO.getUuid());
        //如果验证码为空，提示前端
        if (StringUtils.isEmpty(code)) {
            throw new BusinessException(BaseResponseCodeEnum.AUTH_CODE_TIMEOUT);
        }
        //清除验证码
        redisUtil.delete(loginVO.getUuid());
        //首先判断验证码是否正确
        if (!code.equals(loginVO.getCode())) {
            throw new BusinessException(BaseResponseCodeEnum.AUTH_CODE_ERROR);
        }
        //获取用户
        JwtUser jwtUser = userMapper.getUserInfoByUsername(loginVO.getUsername());
        if (jwtUser == null) {
            throw new BusinessException(BaseResponseCodeEnum.USER_NO_ACCOUNT);
        }
        if (!PasswordUtils.matches(jwtUser.getSalt(), new RSA(rsaPrivateKey, null).decryptStr(loginVO.getPassword(), KeyType.PrivateKey), jwtUser.getPassword())) {
            throw new IncorrectCredentialsException();
        }
        //还需要检测是不是管理员
        List<String> roles = jwtUser.getRoles();
        if (roles.size() > 0) {
            for (String role : roles) {
                if (!"root".equals(role) && !"guest".equals(role) && !"admin".equals(role)) {
                    throw new BusinessException(BaseResponseCodeEnum.USER_NO_ACCOUNT);
                }
            }
        } else {
            throw new BusinessException(BaseResponseCodeEnum.USER_NO_ACCOUNT);
        }
        if (jwtUser.getStatus() == Constant.USER_BLOCKED_CODE) {
            throw new BusinessException(BaseResponseCodeEnum.USER_ACCOUNT_BLOCKED);
        }
        //修改登录时间
        updateById(new User().setId(jwtUser.getId()).setLoginTime(new Date()));
        LoginRespVO loginRespVO=getLoginRespVO(jwtUser,loginVO.getType(),true);
        //将用户登录信息存入数据库，记录操作日志
        long endTime=System.currentTimeMillis();
        Log log=new Log("INFO",endTime-startTime);
        HttpServletRequest request = JwtUtil.getRequest();
        log.setUserId(jwtUser.getId())
                .setUsername(jwtUser.getUsername())
                .setParams("{loginVO: "+loginVO.toString()+"}")
                .setOperation("登录")
                .setMethod("com.antake.controller.UserController.login()")
                .setIp(com.antake.utils.StringUtils.getIp(request))
                .setBrowser(com.antake.utils.StringUtils.getBrowser(request));
        logService.loginSave(log);
        return DataResult.getSuccessResult(loginRespVO);
    }
    private LoginRespVO getLoginRespVO(JwtUser jwtUser,String loginType,boolean needsRefreshToken){
        //将前端需要的数据封装起来，传回去
        LoginRespVO loginRespVO = new LoginRespVO();
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.JWT_USER_ROLES, jwtUser.getRoles());
        claims.put(Constant.JWT_USER_PERMISSIONS, jwtUser.getPermissions());
        claims.put(Constant.JWT_USER_NAME, jwtUser.getUsername());
        String accessToken = JwtUtil.getAccessToken(jwtUser.getId().toString(), claims);
        if (needsRefreshToken){
            String refreshToken = null;
            Map<String, Object> refreshClaims = new HashMap<>();
            refreshClaims.put(Constant.JWT_USER_NAME,jwtUser.getUsername());
            if (Constant.WEB_ACTION.equals(loginType)) {
                refreshToken = JwtUtil.getRefreshToken(jwtUser.getId().toString(), refreshClaims);
            } else {
                refreshToken = JwtUtil.getRefreshAppToken(jwtUser.getId().toString(), refreshClaims);
            }
            loginRespVO.setRefreshToken(refreshToken);
            loginRespVO.setUser(jwtUser);
        }
        loginRespVO.setToken(accessToken);
        loginRespVO.setExpireTime(System.currentTimeMillis()+JwtUtil.getTokenExpireTime());
        //不允许重复登录
        if (singleLogin) {
            kickOutUserById(jwtUser.getId());
        }
        //将用户信息存入redis
        saveOnlineUserInfoIntoRedis(jwtUser, accessToken, JwtUtil.getRequest());
        return loginRespVO;
    }
    @Override
    @Transactional(readOnly = true)
    public DataResult<PageRespVO<User>> listUser(PageVO pageVO) {
        /*        page: 1,
        limit: 20,
        sort: '+id',
        condition: {
          anyFiled: undefined,
          type: undefined,
          role: undefined,
          sex: undefined,
          status: undefined,
          startTime: undefined,
          endTime: undefined
        }*/
        //解析condition拿到角色
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(pageVO.getCondition())) {
            JSONObject jsonObject = JSON.parseObject(pageVO.getCondition());
            if (checkField(jsonObject, "role")) {
                int roleId = (int) jsonObject.get("role");
                List<Long> roleIdList = userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("role_id", roleId)).stream().map(role -> role.getId()).collect(Collectors.toList());
                userQueryWrapper.in("id", roleIdList);
            }
            if (checkField(jsonObject, "anyFiled")) {
                String anyFiled = jsonObject.get("anyFiled").toString();
                userQueryWrapper.like("username", anyFiled)
                        .or().like("id", anyFiled)
                        .or().like("name", anyFiled)
                        .or().like("phone", anyFiled)
                        .or().like("email", anyFiled);
            }
            if (checkField(jsonObject, "sex")) {
                userQueryWrapper.eq("sex", jsonObject.get("sex"));
            }
            if (checkField(jsonObject, "status")) {
                userQueryWrapper.eq("status", jsonObject.get("status"));
            }
            if (checkField(jsonObject, "startTime")) {
                if (!"null".equals(String.valueOf(jsonObject.get("startTime")))) {
                    userQueryWrapper.ge("login_time", jsonObject.get("startTime"))
                            .or().ge("gmt_create", jsonObject.get("startTime"))
                            .or().ge("gmt_modified", jsonObject.get("startTime"));
                }
            }
            if (checkField(jsonObject, "endTime")) {
                if (!"null".equals(String.valueOf(jsonObject.get("endTime")))) {
                    userQueryWrapper.le("login_time", jsonObject.get("endTime"))
                            .or().le("gmt_create", jsonObject.get("endTime"))
                            .or().le("gmt_modified", jsonObject.get("endTime"));
                }
            }
        }
        //解析排序条件
        String sort = pageVO.getSort();
        if (sort.contains("+")) {
            String right = sort.substring(sort.indexOf("+") + 1);
            if (!StringUtils.isEmpty(right)) {
                userQueryWrapper.orderByAsc(right);
            }
            userQueryWrapper.orderByAsc(right);
        } else if (sort.contains("-")) {
            String right = sort.substring(sort.indexOf("-") + 1);
            if (!StringUtils.isEmpty(right)) {
                userQueryWrapper.orderByDesc(right);
            }
        }
        Page<User> userPage = userMapper.selectPage(new Page<User>(pageVO.getPage(), pageVO.getLimit()), userQueryWrapper);
        return DataResult.getSuccessResult(new PageRespVO<User>().setItems(userPage.getRecords()).setTotal(userPage.getTotal()));
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult<JwtUser> getUserInfoByUsername(String username) {
        return DataResult.getSuccessResult(userMapper.getUserInfoByUsername(username));
    }

    @Override
    public void logout(String token) {
        String onlineKey = Constant.USER_ONLINE_KEY.concat(JwtUtil.getUserId(token)).concat("-").concat(token);
        redisUtil.delete(onlineKey);
        SecurityUtils.getSubject().logout();
        redisUtil.delete(Constant.IDENTIFY_CACHE_KEY.concat(JwtUtil.getUserId(token)));
    }

    @Override
    public DataResult changeStatus(long id, int status) {
        //不能禁用管理员
        canNotEditAdmin(id);
        if (updateById(new User().setId(id).setUpdateId(Long.valueOf(JwtUtil.getUserId(JwtUtil.getTokenFromRequest()))).setStatus(status))){
            return DataResult.getSuccessResult();
        }else {
            return DataResult.getFailResult();
        }
    }
    @Transactional(readOnly = true)
    @Override
    public Long checkUserByUsername(String username){
        Long result = userMapper.selectCount(new QueryWrapper<User>().eq("username", username));
        return result>=1L?1L:0L;
    }
    @Override
    public DataResult delete(Long id) {
        //不能删除管理员
        canNotEditAdmin(id);
        boolean update = updateById(new User().setId(id).setUpdateId(Long.valueOf(JwtUtil.getUserId(JwtUtil.getTokenFromRequest()))));
        //如果更新失败，就没必要做后面的操作了
        if(update){
            //删除其他有关联的表的数据
            userRoleMapper.delete(new UpdateWrapper<UserRole>().eq("user_id",id));
            if (userMapper.deleteById(id)>0){
                return DataResult.getSuccessResult();
            }
        }
        return DataResult.getFailResult();
    }

    @Override
    public DataResult add(User user) {
        if (checkUserByUsername(user.getUsername())==1){
            return DataResult.getFailResult("用户名已存在");
        }
        String salt=UUID.randomUUID().toString();
        String password=PasswordUtils.encode(new RSA(rsaPrivateKey, null).decryptStr(user.getPassword(), KeyType.PrivateKey),salt);
        user.setSalt(salt);
        user.setPassword(password);
        user.setCreateId(Long.valueOf(JwtUtil.getUserId(JwtUtil.getTokenFromRequest())));
        if (userMapper.insert(user)>0){
            return DataResult.getSuccessResult();
        }else {
            return DataResult.getFailResult();
        }
    }

    @Override
    public DataResult updateUser(User user) {
        //不能修改管理员信息
        canNotEditAdmin(user.getId());
        //处理数据过滤不能更改的
        /*tempData.loginTime = undefined
        tempData.gmtCreate = undefined
        tempData.gmtModified = undefined
        tempData.createId = undefined
        tempData.updateId = undefined*/
        //防止改名的时候与别人一致了
        User oldUser = userMapper.selectOne(new QueryWrapper<User>().eq("id", user.getId()));
        if (!oldUser.getUsername().equals(user.getUsername())){
            if (checkUserByUsername(user.getUsername())==1){
                return DataResult.getFailResult("用户名已存在");
            }
        }
        user.setLoginTime(null);
        user.setGmtCreate(null);
        user.setGmtModified(null);
        user.setCreateId(null);
        user.setUpdateId(Long.valueOf(JwtUtil.getUserId(JwtUtil.getTokenFromRequest())));
        if (!StringUtils.isEmpty(user.getPassword())){
            String salt=UUID.randomUUID().toString();
            String password=PasswordUtils.encode(new RSA(rsaPrivateKey, null).decryptStr(user.getPassword(), KeyType.PrivateKey),salt);
            user.setSalt(salt).setPassword(password);
        }
        int result = userMapper.updateById(user);
        if (result>0){
            return DataResult.getSuccessResult();
        }else {
            return DataResult.getFailResult();
        }
    }

    private void saveOnlineUserInfoIntoRedis(JwtUser user, String token, HttpServletRequest request) {
        //获取用户ip地址
        String ip = com.antake.utils.StringUtils.getIp(request);
        //获取用户浏览器
        String browser = com.antake.utils.StringUtils.getBrowser(request);
        //根据ip地址查询归属地
        String cityInfo = com.antake.utils.StringUtils.getIpArea(ip);
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setId(user.getId())
                .setUsername(user.getUsername())
                .setName(user.getName())
                .setIp(ip)
                .setBrowser(browser)
                .setCityInfo(cityInfo)
                .setOnlineTime(new Date());
        //校验
        redisUtil.set(Constant.USER_ONLINE_KEY + user.getId() + "-" + token, onlineUser, JwtUtil.getTokenExpireTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * @Description: 检测y用户是否已经登录，已经登录踢下线
     * @Param: [username, token]
     * @return: void
     * @Author: MiZiMi
     * @Date: 2020/5/11 15:24
     */
    @Override
    public void kickOutUserById(Long id) {
        if (id!=null){
            //判断是是不是踢出的管理，是不是判断是不是管理登录，是就可以踢出，不是就不能
            if (id==1 && JwtUtil.getTokenFromRequest()!=null &&!"1".equals(JwtUtil.getUserId(JwtUtil.getTokenFromRequest()))){
                return;
            }
            //首先拿到指定账号所有登陆的key
            Set<String> keys = redisUtil.keys(Constant.USER_ONLINE_KEY + id + "-*");
            //删除已经登陆的keys
            if (keys.size() > 0) {
                long deleteCount = 0L;
                try {
                    deleteCount = redisUtil.delete(keys);
                } catch (Exception e) {
                    log.error("checkUserLogined...error{}", e);
                }
                log.info("kickOutUserById-" + id + "-删除之前已经存在的key:" + deleteCount + "个");
            }
        }
    }

    @Override
    public DataResult listOnlineUser(PageVO pageVO) {
        int page = Math.toIntExact(pageVO.getPage());
        int limit = Math.toIntExact(pageVO.getLimit());
        Set<String> keys = redisUtil.keys(Constant.USER_ONLINE_KEY + "*");
        if (keys.size()<(page-1)*limit){
            return DataResult.getSuccessResult(new PageRespVO<>().setTotal((long) keys.size()).setItems(null));
        }
        int newLimit=0;
        if (keys.size()<page*limit){
            newLimit=limit-(page*limit)+keys.size();
        }else {
            newLimit=limit;
        }
        List<String> collect = Arrays.asList(keys.toArray()).stream().map(o -> o.toString()).collect(Collectors.toList());
        List<String> list = collect.subList((page - 1) * limit, newLimit);
        if (list.size()>0){
            try {
                List<Object> onlineUsers = redisUtil.mget(list);
                if (!StringUtils.isEmpty(pageVO.getCondition())) {
                    JSONObject jsonObject = JSON.parseObject(pageVO.getCondition());
                    if (checkField(jsonObject, "anyFiled")) {
                        String anyFiled = jsonObject.get("anyFiled").toString();
                        List<Object> filterList = onlineUsers.stream().filter(user -> {
                            if (user instanceof OnlineUser) {
                                if (((OnlineUser) user).getUsername().indexOf(anyFiled) != -1) {
                                    return true;
                                }
                                if (((OnlineUser) user).getName()!=null){
                                    if (((OnlineUser) user).getName().indexOf(anyFiled) != -1) {
                                        return true;
                                    }
                                }
                                if (((OnlineUser) user).getIp().indexOf(anyFiled) != -1) {
                                    return true;
                                }
                                if (((OnlineUser) user).getBrowser().indexOf(anyFiled) != -1) {
                                    return true;
                                }
                                if (((OnlineUser) user).getCityInfo().indexOf(anyFiled) != -1) {
                                    return true;
                                }
                            }
                            return false;
                        }).collect(Collectors.toList());
                        return DataResult.getSuccessResult(new PageRespVO<>().setTotal((long) keys.size()).setItems(filterList));
                    }
                }
                return DataResult.getSuccessResult(new PageRespVO<>().setTotal((long) keys.size()).setItems(onlineUsers));
            }catch (Exception e){
                log.error("listOnlineUser...error{}", e);
            }
        }
        return DataResult.getFailResult();
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult<User> getUserInfoByUserId(Long id) {
        return DataResult.getSuccessResult(userMapper.selectById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult refreshToken(String refreshToken,String loginType) {
        //获取用户
        JwtUser jwtUser = userMapper.getUserInfoByUsername(JwtUtil.getUsername(refreshToken));
        LoginRespVO loginRespVO=getLoginRespVO(jwtUser,loginType,false);
        return DataResult.getSuccessResult(loginRespVO);
    }
    @Override
    @Transactional(readOnly = true)
    public DataResult getUserRoleByUserId(Long userId) {
        List<Integer> roleIds = userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("user_id", userId)).stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList());
        return DataResult.getSuccessResult(roleIds);
    }

    @Override
    public DataResult updateUserRole(Long userId, List<Integer> roleIds) {
        //首先清空之前的角色信息
        userRoleMapper.delete(new UpdateWrapper<UserRole>().eq("user_id",userId));
        List<UserRole> userRoles=new ArrayList<>();
        for (Integer roleId : roleIds) {
            userRoles.add(new UserRole().setUserId(userId).setRoleId(roleId));
        }
        boolean result = userRoleService.saveBatch(userRoles);
        if  (result){
            return DataResult.getSuccessResult();
        }
        return DataResult.getFailResult();
    }

    private  boolean checkField(JSONObject jsonObject, String field) {
        return jsonObject.containsKey(field) && !StringUtils.isEmpty(String.valueOf(jsonObject.get(field)));
    }

    private void canNotEditAdmin(Long id){
        if (!"1".equals(JwtUtil.getUserId(JwtUtil.getTokenFromRequest()))){
            //检测是不是禁用的管理员
            Set<Long> userIds = userRoleMapper.selectList(new QueryWrapper<UserRole>().in("role_id", 1, 2)).stream().map(role -> role.getUserId()).collect(Collectors.toSet());
            if (userIds.contains(id)){
                throw new BusinessException(BaseResponseCodeEnum.USER_UNAUTHORIZED);
            }
        }
    }
}
