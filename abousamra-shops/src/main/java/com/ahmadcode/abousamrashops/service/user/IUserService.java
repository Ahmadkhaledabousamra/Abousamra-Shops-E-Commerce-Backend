package com.ahmadcode.abousamrashops.service.user;

import com.ahmadcode.abousamrashops.dto.UserDto;
import com.ahmadcode.abousamrashops.model.User;
import com.ahmadcode.abousamrashops.request.user.CreateUserRequest;
import com.ahmadcode.abousamrashops.request.user.UpdateUserRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUseroDto(User user);

    User getAuthenticatedUser();
}
