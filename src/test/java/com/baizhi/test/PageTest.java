package com.baizhi.test;

import com.baizhi.entity.Category;
import com.baizhi.mapper.CategoryMapper;
import com.baizhi.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import util.MinioUtil;

import java.util.Arrays;
import java.util.HashMap;

@SpringBootTest
public class PageTest {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void test2() {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("levels", 2).eq("parent_id", 1);
//        if (parentId != null) {
//            wrapper.eq("parent_id", 1);
//        }
        wrapper.select("id", "cate_name", "levels", "parent_id");
        wrapper.orderByDesc(Arrays.asList("gmt_create", "id"));
        Page<Category> categoryPage = new Page<>(1, 3);
        Page<Category> pages = categoryMapper.selectPage(categoryPage, wrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("result", pages.getRecords());
        map.put("total", pages.getTotal());
        System.out.println(map);
    }

    @Test
    public void test1() {
        String duxin = MinioUtil.getPreviewFileUrl("duxin", "基础数据结构-001-二分查找-算法描述.mp4");
        System.out.println(duxin);
    }
}
