package com.ahmadcode.abousamrashops.request.user;

import com.ahmadcode.abousamrashops.model.Cart;
import com.ahmadcode.abousamrashops.model.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
}
