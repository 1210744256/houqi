package com.baizhi.mapper;

import com.baizhi.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 12107
* @description 针对表【yx_video】的数据库操作Mapper
* @createDate 2023-09-05 11:27:23
* @Entity com.baizhi.entity.Video
*/
public interface VideoMapper extends BaseMapper<Video> {
    public List<Video> searchVideo(@Param("page") int page, @Param("size") int size, @Param("title") String title);
}




