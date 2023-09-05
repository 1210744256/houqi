package com.baizhi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baizhi.entity.Log;
import com.baizhi.service.LogService;
import com.baizhi.mapper.LogMapper;
import org.springframework.stereotype.Service;

/**
* @author 12107
* @description 针对表【yx_log】的数据库操作Service实现
* @createDate 2023-09-05 11:27:23
*/
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
    implements LogService{

}




