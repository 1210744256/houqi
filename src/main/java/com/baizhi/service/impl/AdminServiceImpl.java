package com.baizhi.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baizhi.config.RedisConstants;
import com.baizhi.dto.AdminResponse;
import com.baizhi.dto.LoginRequest;
import com.baizhi.dto.Result;
import com.baizhi.entity.Admin;
import com.baizhi.mapper.AdminMapper;
import com.baizhi.service.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.*;

/**
 * @author 12107
 * @description 针对表【yx_admin】的数据库操作Service实现
 * @createDate 2023-09-05 11:27:23
 */
@Service
@Transactional
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
        implements AdminService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result login(String code, LoginRequest loginResult, String session) {
        log.debug("loginresult:{}" + loginResult);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object storageCode = valueOperations.get(RedisConstants.CODE_PREFIX_VALUE + session);
        log.debug("存储的code:" + storageCode);
        if (ObjectUtil.isEmpty(storageCode)) {
            return new Result().error(null, "请先获取验证码");
        }
        if (StrUtil.isEmpty(code)) {
            return new Result().error(null, "请输入验证码");
        }
        if (!code.equalsIgnoreCase(storageCode.toString())) {
            return new Result().error(null, "验证码错误");
        }
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username", loginResult.username);
//        adminQueryWrapper.eq("password", loginResult.password);
        Admin admin = adminMapper.selectOne(adminQueryWrapper);
        log.debug("admin:{}" + admin);
        if (admin == null) {
            return new Result().error(null, "用户名错误");
        }
//        用户名存在进行密码
        String salt = admin.getSalt();
        String password = loginResult.getPassword();
        for (int i = 0; i < 3; i++) {

            password= DigestUtils.md5DigestAsHex((salt+password).getBytes());
        }
        if(!admin.getPassword().equals(password)){
            return new Result().error(null, "密码错误");
        }
        AdminResponse adminResponse = new AdminResponse();
        BeanUtils.copyProperties(admin, adminResponse);
        return new Result().ok(adminResponse);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String, Object> queryByPage(int page, int limit) {
        Map<String, Object> map = new HashMap<>();
        Page<Admin> adminPage = new Page<>(page, limit);
//        倒序条件
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(Arrays.asList("gmt_create", "id"));
        Page<Admin> adminPage1 = adminMapper.selectPage(adminPage, wrapper);
        List<Admin> records = adminPage1.getRecords();
        List<AdminResponse> responses = new ArrayList<>();
        for (Admin admin : records) {
            AdminResponse adminResponse = new AdminResponse();
            BeanUtils.copyProperties(admin, adminResponse);
            responses.add(adminResponse);
        }
        long total = adminPage1.getTotal();
        map.put("total", total);
        map.put("result", responses);

        //分页查询后，将分页信息放
//        BeanUtils.copyProperties(records,responses);
        return map;
    }

    @Override
    public Result add(LoginRequest loginRequest) {
//        DigestUtils.md5Digest();
//        先查询用户名是否存在
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username", loginRequest.username);
        Admin admin = adminMapper.selectOne(adminQueryWrapper);
        if (admin != null) {
            return new Result().error(null, "用户名已存在");
        }
        //        密码加密
//        盐
        String salt = UUID.randomUUID().toString().substring(0,16);
        String password = loginRequest.getPassword();
        for (int i = 0; i < 3; i++) {
            password= DigestUtils.md5DigestAsHex((salt+password).getBytes());
        }
        Admin admin1 = new Admin();
        admin1.setPassword(password);
        admin1.setUsername(loginRequest.getUsername());
        admin1.setSalt(salt);
        adminMapper.insert(admin1);
        return new Result().ok();
    }
}




