package com.ahmadcode.abousamrashops.repository;

import com.ahmadcode.abousamrashops.model.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> {
}