package com.example.ecom.controllers.admin;

import com.example.ecom.dto.CategoryDto;
import com.example.ecom.entity.category;
import com.example.ecom.services.jwt.admin.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping("category")
    public ResponseEntity<category> createCategory(@RequestBody CategoryDto categoryDto){
        category cat = categoryService.createCategory(categoryDto);
        System.out.println(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cat);
    }


    @GetMapping("getCategory")
    public List<category> getAllCategories(){
        return categoryService.getCategories();
    }

}
