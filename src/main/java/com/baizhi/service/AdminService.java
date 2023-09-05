package com.baizhi.service;

import com.baizhi.dto.AdminResponse;
import com.baizhi.dto.LoginRequest;
import com.baizhi.dto.Result;
import com.baizhi.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 12107
* @description 针对表【yx_admin】的数据库操作Service
* @createDate 2023-09-05 11:27:23
*/
public interface AdminService extends IService<Admin> {
    Result login(String code, LoginRequest loginResult, String session);
//    List<>
    List<AdminResponse> queryByPage(int page, int limit);
//    添加管理员
    Result add(LoginRequest loginRequest);
}
