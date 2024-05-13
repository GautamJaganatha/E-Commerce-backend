package com.example.ecom.services.jwt.admin.category;

import com.example.ecom.dto.CategoryDto;
import com.example.ecom.entity.category;
import com.example.ecom.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;


    public category createCategory(CategoryDto categoryDto){
        category cat = new category();
        cat.setName(categoryDto.getName());
        cat.setDescription(categoryDto.getDescription());



        return categoryRepository.save(cat);
    }


    public List<category> getCategories(){
        return  categoryRepository.findAll();
    }

}
