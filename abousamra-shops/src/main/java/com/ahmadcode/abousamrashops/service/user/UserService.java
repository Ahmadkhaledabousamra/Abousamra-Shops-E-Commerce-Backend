package com.ahmadcode.abousamrashops.service.user;

import com.ahmadcode.abousamrashops.dto.UserDto;
import com.ahmadcode.abousamrashops.exception.AlReadyExistsException;
import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.User;
import com.ahmadcode.abousamrashops.repository.UserRepository;
import com.ahmadcode.abousamrashops.request.user.CreateUserRequest;
import com.ahmadcode.abousamrashops.request.user.UpdateUserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User Not Found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.ofNullable(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlReadyExistsException("Oops! "+request.getEmail()+"  alReady exists!!"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
       return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    return userRepository.save(existingUser);
                }).orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));

    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, ()-> {
                    throw new ResourceNotFoundException("User No Found!");
                });

    }

    @Override
    public UserDto convertUseroDto(User user){
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
