package com.baizhi.service;

import com.baizhi.dto.Result;
import com.baizhi.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 12107
* @description 针对表【yx_log】的数据库操作Service
* @createDate 2023-09-05 11:27:23
*/
public interface LogService extends IService<Log> {
    public Result queryByPage(int page, int size, String startTime, String endTime);
}
