package com.baizhi.controller;

import com.baizhi.dto.Result;
import com.baizhi.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/log")
@RestController
public class LogController {
    @Autowired
    private LogService logService;
//    /queryByPage
    @GetMapping("/queryByPage")
    public Result queryByPage(int page, int size, String startTime,String endTime){
        try {
        return logService.queryByPage(page, size, startTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error(null, "网络错误");
        }
    }

}
