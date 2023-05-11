package com.project.capstone.Api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/categories")
public interface CategoryApi {

    @RequestMapping("/viewCategories")
    public ResponseEntity<?> allCategories();
}
