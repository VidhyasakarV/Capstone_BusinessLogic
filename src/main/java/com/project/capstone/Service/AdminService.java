package com.project.capstone.Service;

import com.project.capstone.UserDto.MsgDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface AdminService {
    public ResponseEntity<MsgDto> addCategories(@PathVariable String item);

    public ResponseEntity<MsgDto> deleteCategory(@PathVariable String item);

    public ResponseEntity<?> deleteAllCategories();

}
