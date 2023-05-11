package com.project.capstone.Api;

import com.project.capstone.UserDto.MsgDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/categories/admin")
public interface AdminApi {
    @PutMapping("/addCategories/{item}")
    public ResponseEntity<MsgDto> addCategories(@PathVariable String item);

    @DeleteMapping("/deleteCategory/{item}")
    public ResponseEntity<MsgDto> deleteCategory(@PathVariable String item);

    @DeleteMapping("/categories/deleteall")
    public ResponseEntity<MsgDto> deleteAllCategories();
}
