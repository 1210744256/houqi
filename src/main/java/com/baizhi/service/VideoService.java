package com.baizhi.service;

import com.baizhi.dto.Result;
import com.baizhi.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 12107
* @description 针对表【yx_video】的数据库操作Service
* @createDate 2023-09-05 11:27:23
*/
public interface VideoService extends IService<Video> {
    public Result upload(@RequestBody Video video, MultipartFile multipartFile);
    public Result searchVideo(int page,int size,String title);
}
