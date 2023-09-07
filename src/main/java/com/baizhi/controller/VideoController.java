package com.baizhi.controller;

import com.baizhi.dto.Result;
import com.baizhi.entity.Video;
import com.baizhi.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/video")
@Slf4j
public class VideoController {
    @Autowired
    private VideoService videoService;

    //    /video/upload
    @PostMapping("upload")
    public Result upload(Video video, MultipartFile file) {
        try {
            videoService.upload(video, file);
            return new Result().ok();

        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error("网络错误");
        }
    }

    //    /video/searchVideo分页查询视频
    @GetMapping("searchVideo")
    public Result searchVideo(int page, int size, String title) {
        try {
            Result result = videoService.searchVideo(page, size, title);
            log.debug("map为:{}"+result.getData());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error("网络错误");
        }
    }

}
