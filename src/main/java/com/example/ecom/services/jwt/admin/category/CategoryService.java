package com.example.ecom.services.jwt.admin.category;

import com.example.ecom.dto.CategoryDto;
import com.example.ecom.entity.category;

import java.util.List;

public interface CategoryService {

    category createCategory(CategoryDto categoryDto);


    List<category> getCategories();
}
