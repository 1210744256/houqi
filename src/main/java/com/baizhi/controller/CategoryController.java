package com.baizhi.controller;

import com.baizhi.dto.Result;
import com.baizhi.entity.Category;
import com.baizhi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/queryLevels")
    public Result queryLevels(Integer page, Integer size, int levels, Integer parentId) {
        try {
            return categoryService.queryLevels(page,size,levels,parentId);
        }catch (Exception e){
            e.printStackTrace();
            return new Result().error(null,"网络错误");
        }
    }
//    **Path：**/api/category/addCategory添加类别
    @PostMapping("/addCategory")
    public Result addCategory(@RequestBody Category category){
        try {
            categoryService.save(category);
            return new Result().ok();
        }catch (Exception e){
            e.printStackTrace();
            return new Result().error(null,"网络错误");
        }
    }
//    /category/editName修改类别名称
    @PostMapping("/editName")
    public Result editName(@RequestBody Category category){
        try {
            categoryService.updateById(category);
            return new Result().ok();
        }catch (Exception e){
            e.printStackTrace();
            return new Result().error(null,"网络错误");
        }
    }
//    /category/deleteCategory删除类别
    @GetMapping("/deleteCategory")
    public Result deleteCategory(Integer id){
        try {
            categoryService.removeById(id);
            return new Result().ok();
        }catch (Exception e){
            e.printStackTrace();
            return new Result().error(null,"网络错误");
        }
    }
}
