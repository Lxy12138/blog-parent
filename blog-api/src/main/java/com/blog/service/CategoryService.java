package com.blog.service;

import com.blog.vo.CategoryVo;
import com.blog.vo.Result;

public interface CategoryService {


    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
