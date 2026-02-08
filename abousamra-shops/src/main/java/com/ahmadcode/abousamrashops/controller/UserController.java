package com.ahmadcode.abousamrashops.controller;

import com.ahmadcode.abousamrashops.dto.UserDto;
import com.ahmadcode.abousamrashops.exception.AlReadyExistsException;
import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.User;
import com.ahmadcode.abousamrashops.request.user.CreateUserRequest;
import com.ahmadcode.abousamrashops.request.user.UpdateUserRequest;
import com.ahmadcode.abousamrashops.response.ApiResponse;
import com.ahmadcode.abousamrashops.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
       try {
           User user = userService.getUserById(userId);
           UserDto userDto = userService.convertUseroDto(user);
           return ResponseEntity.ok(new ApiResponse("Success" , userDto));
       }catch (ResourceNotFoundException e){
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
       }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUseroDto(user);
            return ResponseEntity.ok(new ApiResponse("Created Successfully!",userDto));
        }catch (AlReadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request , @PathVariable Long userId){
        try {
            User updatedUser = userService.updateUser(request,userId);
            UserDto userDto = userService.convertUseroDto(updatedUser);
            return ResponseEntity.ok(new ApiResponse("Updated Successfully!",userDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
             userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Deleted Successfully!",null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }





}

