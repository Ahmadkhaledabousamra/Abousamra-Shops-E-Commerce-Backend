package com.ahmadcode.abousamrashops.exception;

import java.util.function.Supplier;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }


}
