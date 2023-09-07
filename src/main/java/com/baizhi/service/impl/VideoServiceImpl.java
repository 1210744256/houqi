package com.baizhi.service.impl;

import com.baizhi.dto.Result;
import com.baizhi.entity.Video;
import com.baizhi.mapper.VideoMapper;
import com.baizhi.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import util.MinioUtil;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author 12107
 * @description 针对表【yx_video】的数据库操作Service实现
 * @createDate 2023-09-05 11:27:23
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService {
//    @Autowired
//    private MinioUtil minioUtil;
    @Autowired
    private VideoMapper videoMapper;
    private static final String BUCKET_NAME = "duxin";

    @Override
    public Result upload(Video video, MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        String afterfix = originalFilename.split("\\.")[1];
        uuid = uuid + "." + afterfix;
        MinioUtil.minioUpload(multipartFile,uuid,BUCKET_NAME);
        video.setStatus(1);
        video.setCoverPath("duxin");
        video.setVideoPath(uuid);
        videoMapper.insert(video);
        return null;
    }

    @Override
    public Result searchVideo(int page, int size, String title) {
        List<Video> videos = videoMapper.searchVideo((page - 1) * size, size, title);
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.like("title", title);
        Long total = videoMapper.selectCount(wrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("result",videos);
        map.put("total",total);
        return new Result().ok(map);
    }
}




