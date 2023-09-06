package com.baizhi.service.impl;

import com.baizhi.dto.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baizhi.entity.Category;
import com.baizhi.service.CategoryService;
import com.baizhi.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author 12107
 * @description 针对表【yx_category】的数据库操作Service实现
 * @createDate 2023-09-05 11:27:23
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result queryLevels(Integer page, Integer size, int levels, Integer parentId) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.select("id", "cate_name", "levels", "parent_id");
        wrapper.orderByDesc(Arrays.asList("gmt_create", "id"));
        if (page == null) {
//            如果没有页数进入非分页查模式
            List<Category> categories = categoryMapper.selectList(wrapper);
            return new Result().ok(categories);
        }
//        如果有页数
        Page<Category> categoryPage = new Page<>(page,size);
        Page<Category> pages = categoryMapper.selectPage(categoryPage, wrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("result",pages.getRecords());
        map.put("total",pages.getTotal());
        return new Result().ok(map);
    }
}




