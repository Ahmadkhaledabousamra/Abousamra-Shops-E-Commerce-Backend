package com.ahmadcode.abousamrashops.controller;

import com.ahmadcode.abousamrashops.exception.AlReadyExistsException;
import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.Category;
import com.ahmadcode.abousamrashops.response.ApiResponse;
import com.ahmadcode.abousamrashops.service.category.CategoryService;
import com.ahmadcode.abousamrashops.service.category.ICategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/categories")

public class CategoryController  {
    @Autowired
    private  ICategoryService categoryService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found!", categories));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", INTERNAL_SERVER_ERROR));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        try {
            Category theCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Sucess!", theCategory));
        }catch (AlReadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping("/category/{CategoryId}/id")
    public ResponseEntity<ApiResponse> geCategoryById(@PathVariable Long CategoryId){
        try{
            Category theCategory =  categoryService.getCategoryById(CategoryId);
            return ResponseEntity.ok(new ApiResponse("Found!", theCategory));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/category/{name}/name")
    public ResponseEntity<ApiResponse> geCategoryByName(@PathVariable String name){
        try{
            Category theCategory =  categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found!", theCategory));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try{
             categoryService.deleteCategoryById(id);
             return ResponseEntity.ok(new ApiResponse("Found!", null));
        }catch (ResourceNotFoundException e){
             return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id , @RequestBody Category category){
        try{
           Category updatedCategory =  categoryService.updateCategory(category,id);
            return ResponseEntity.ok(new ApiResponse("Found!", updatedCategory));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


}
