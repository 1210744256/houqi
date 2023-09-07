package com.baizhi.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baizhi.config.RedisConstants;
import com.baizhi.dto.LoginRequest;
import com.baizhi.dto.Result;
import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import util.VerifyCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequestMapping("/api/admin")
@RestController
@Slf4j
//@ResponseBody
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RedisTemplate redisTemplate;

    //    /api/admin/getVerifyImage
    @GetMapping("/getVerifyImage")
    public Result getVerifyImage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            String cd = VerifyCodeUtil.generateVerifyCode(4);
            String base64Image = VerifyCodeUtil.createBase64Image(cd);
            String id = session.getId();
            System.out.println(id);
            LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
            BufferedImage image = lineCaptcha.getImage();
//            String code = lineCaptcha.getCode();
//            System.out.println(code);
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set(RedisConstants.CODE_PREFIX_VALUE + id, cd, 30, TimeUnit.MINUTES);
            return new Result().ok(base64Image);
        } catch (Exception e) {
            return new Result().error(null, "网络错误");
        }
    }

    //    /api/admin/login
//    管理员登录
    @PostMapping("/login")
    public Result login(String code, @RequestBody LoginRequest loginRequest, HttpSession session) {
        String id = session.getId();
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setPassword(password);
//        loginRequest.setUsername(username);
        Result login = adminService.login(code, loginRequest, id);
        if (login.getStatus()) {
            redisTemplate.opsForValue().set(RedisConstants.LOGIN_PREFIX_VALUE + id, login.getData(), 30, TimeUnit.MINUTES);
        }
        return login;
    }

    //    分页查询所有管理员
//    /api/admin/queryByPage
    @GetMapping("/queryByPage")
    public Result queryByPage(Integer page, Integer size) {
        try {
            QueryWrapper<Admin> wrapper = new QueryWrapper<>();
            Map<String, Object> map = adminService.queryByPage(page, size);
            return new Result().ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }
    }

    //    /api/admin/update
//    退出
//    /api/admin/logout
    @GetMapping("/logout")
    public Result logout(HttpSession session) {
        try {
            String id = session.getId();
            redisTemplate.delete(RedisConstants.LOGIN_PREFIX_VALUE + id);
            return new Result().ok();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }

    }

    //    /api/admin/me
//    获取当前登录的管理员的信息
    @GetMapping("/me")
    public Result me(HttpSession session) {
        try {
            String id = session.getId();
            Object o = redisTemplate.opsForValue().get(RedisConstants.LOGIN_PREFIX_VALUE + id);
            return new Result().ok(o);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }

    }

    //    添加管理员
//    /api/admin/addAdmin
    @PostMapping("/addAdmin")
    public Result addAdmin(@RequestBody LoginRequest loginRequest) {
        try {
//            LoginRequest loginRequest = new LoginRequest();
//            loginRequest.setUsername(username);
//            loginRequest.setPassword(password);
            return adminService.add(loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }

    }

    //    修改管理员密码
//    /api/admin/changePassword
    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody Admin admin) {
        try {
            //        密码加密
//        盐
            String salt = UUID.randomUUID().toString().substring(0,16);
            String password = admin.getPassword();
            for (int i = 0; i < 3; i++) {
                password = DigestUtils.md5DigestAsHex((salt + password).getBytes());
            }
            admin.setPassword(password);
            admin.setSalt(salt);
            adminService.updateById(admin);
            return new Result().ok();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }
    }

    //    删除管理员
//    /api/admin/deleteAdmin
    @GetMapping("/deleteAdmin")
    public Result deleteAdmin(int id) {
        try {
            adminService.removeById(id);
            return new Result().ok();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }
    }


}
