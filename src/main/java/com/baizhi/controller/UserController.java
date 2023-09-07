package com.baizhi.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baizhi.config.RedisConstants;
import com.baizhi.dto.PhoneDto;
import com.baizhi.dto.Result;
import com.baizhi.dto.UserResponse;
import com.baizhi.entity.User;
import com.baizhi.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    //    /api/user/queryByPage分页查询所有用户
    @GetMapping("queryByPage")
    public Result queryByPage(int page, int size,HttpServletRequest request) {
        try {
            String realPath = request.getServletContext().getRealPath("img");
            System.out.println(realPath);
            return userService.queryByPage(page, size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }
    }

    //    /api/user/toggleStatus
    @PostMapping("toggleStatus")
    public Result toggleStatus(@RequestBody UserResponse userResponse) {
        try {
            return userService.toggleStatus(userResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }
    }

    //    user/sendVerificationCode发送手机验证码
    @PostMapping("sendVerificationCode")
    public Result sendVerificationCode(@RequestBody Map map) {
        try {
            //            先查看用户是否存在
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("phone",map.get("phone"));
            long count = userService.count(wrapper);
            if(count==0)return new Result().error(null, "没有此用户");
            String code = RandomUtil.randomNumbers(6);
            redisTemplate.opsForValue().set(RedisConstants.CODE_PREFIX_VALUE+map.get("phone"),code,2, TimeUnit.MINUTES);
            return new Result().ok();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }
    }
//    user/checkVerificationCode验证手机验证码
    @PostMapping("checkVerificationCode")
    public Result checkVerificationCode(@RequestBody PhoneDto phoneDto) {
        try {
            String code = (String) redisTemplate.opsForValue().get(RedisConstants.CODE_PREFIX_VALUE+phoneDto.getPhone());
//            先查看用户是否存在
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("phone",phoneDto.getPhone());
            long count = userService.count(wrapper);
            if(count==0)return new Result().error(null, "没有此用户");
            if(StrUtil.isEmpty(code)){
                return new Result().error(null, "请先获取验证码");
            }
            if(StrUtil.isEmpty(phoneDto.getCode())){
                return new Result().error(null, "请输入验证码");
            }
            if(!StrUtil.equals(phoneDto.getCode(),code)){
                return new Result().error(null, "验证码错误请重新输入验证码");
            }
            return new Result().ok();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }
    }

}
