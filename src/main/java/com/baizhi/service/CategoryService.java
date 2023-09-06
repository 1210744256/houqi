package com.baizhi.service;

import com.baizhi.dto.Result;
import com.baizhi.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 12107
* @description 针对表【yx_category】的数据库操作Service
* @createDate 2023-09-05 11:27:23
*/
public interface CategoryService extends IService<Category> {
    public Result queryLevels(Integer page, Integer size, int levels, Integer parentId);
}
