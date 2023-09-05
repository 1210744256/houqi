package com.baizhi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baizhi.entity.User;
import com.baizhi.service.UserService;
import com.baizhi.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 12107
* @description 针对表【yx_user】的数据库操作Service实现
* @createDate 2023-09-05 11:27:23
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




