package com.baizhi.service.impl;

import com.baizhi.dto.Result;
import com.baizhi.dto.UserResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baizhi.entity.User;
import com.baizhi.service.UserService;
import com.baizhi.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author 12107
 * @description 针对表【yx_user】的数据库操作Service实现
 * @createDate 2023-09-05 11:27:23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result queryByPage(int page, int size) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(Arrays.asList("gmt_create","id"));
        Page<User> userPage = new Page<>(page, size);
        Page<User> userPage1 = userMapper.selectPage(userPage, wrapper);
        List<User> records = userPage1.getRecords();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User record : records) {
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(record,userResponse);
            if(record.getStatus()==1){
              userResponse.setStatus(true);
            }
            userResponse.setHeadImg("img"+"\\"+record.getHeadImg());
            userResponse.setCreateTime(record.getGmtCreate());
            userResponses.add(userResponse);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("result",userResponses);
        map.put("total",userPage1.getTotal());

        return new Result().ok(map);
    }

    @Override
    public Result toggleStatus(UserResponse userResponse) {
        User user = new User();
        user.setId(userResponse.getId());
        if(userResponse.isStatus()){
            user.setStatus(1);
        }else {
            user.setStatus(0);
        }
        userMapper.updateById(user);
        return new Result().ok();
    }
}




