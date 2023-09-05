package com.baizhi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baizhi.entity.Category;
import com.baizhi.service.CategoryService;
import com.baizhi.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 12107
* @description 针对表【yx_category】的数据库操作Service实现
* @createDate 2023-09-05 11:27:23
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




