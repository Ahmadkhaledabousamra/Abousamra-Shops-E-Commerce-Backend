package com.ahmadcode.abousamrashops.security.user;

import com.ahmadcode.abousamrashops.model.User;
import com.ahmadcode.abousamrashops.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ShopUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail( email))
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found!"));
        return ShopUserDetails.buildUserDetails(user);

    }
}
