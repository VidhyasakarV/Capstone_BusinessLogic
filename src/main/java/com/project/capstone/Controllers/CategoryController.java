package com.project.capstone.Controllers;

import com.project.capstone.Api.CategoryApi;
import com.project.capstone.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryApi {

    @Autowired
    CategoryService categoryService;

    @Override
    public ResponseEntity<?> allCategories() {
        return categoryService.allCategories();
    }
}
