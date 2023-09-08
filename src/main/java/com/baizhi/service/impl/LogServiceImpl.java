package com.baizhi.service.impl;

import com.baizhi.dto.LogResponse;
import com.baizhi.dto.Result;
import com.baizhi.entity.Log;
import com.baizhi.mapper.LogMapper;
import com.baizhi.service.LogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 12107
 * @description 针对表【yx_log】的数据库操作Service实现
 * @createDate 2023-09-05 11:27:23
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
        implements LogService {
    @Autowired
private LogMapper logMapper;
    @Override
    public Result queryByPage(int page, int size, String startTime, String endTime) {
        Page<Log> logPage = new Page<>(page, size);
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        wrapper.ge("gmt_create", startTime);
        wrapper.le("gmt_create", endTime);
        Page<Log> logPage1 = logMapper.selectPage(logPage, wrapper);
        HashMap<String, Object> map = new HashMap<>();
        List<Log> records = logPage1.getRecords();
        List<LogResponse> list=new ArrayList<>();
        for (Log record : records) {
            LogResponse logResponse = new LogResponse();
            BeanUtils.copyProperties(record,logResponse);
            logResponse.setCreateTime(record.getGmtCreate());
            if(record.getOptionStatus()==1){
                logResponse.setOptionStatus(true);
            }
            list.add(logResponse);
        }
        map.put("result",list);
        map.put("total",logPage1.getTotal());
        return new Result().ok(map);
    }
}




