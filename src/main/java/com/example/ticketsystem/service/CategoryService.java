package com.example.ticketsystem.service;

import com.example.ticketsystem.model.Category;
import com.example.ticketsystem.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByIdAsc();
    }


    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}