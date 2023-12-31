package com.baizhi.service;

import com.baizhi.dto.Result;
import com.baizhi.dto.UserResponse;
import com.baizhi.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 12107
* @description 针对表【yx_user】的数据库操作Service
* @createDate 2023-09-05 11:27:23
*/
public interface UserService extends IService<User> {
    public Result queryByPage(int page, int size);
    public Result toggleStatus(UserResponse userResponse);
}
