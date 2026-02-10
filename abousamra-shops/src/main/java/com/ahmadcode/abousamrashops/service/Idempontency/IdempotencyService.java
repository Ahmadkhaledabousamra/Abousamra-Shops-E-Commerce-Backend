package com.ahmadcode.abousamrashops.service.Idempontency;

import com.ahmadcode.abousamrashops.model.IdempotencyKey;
import com.ahmadcode.abousamrashops.repository.IdempotencyKeyRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class IdempotencyService {

    private final IdempotencyKeyRepository repository;

    public IdempotencyService(IdempotencyKeyRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<ResponseEntity<String>> handleExistingKey(String key) {
        return repository.findById(key)
                .filter(record -> record.getStatus() == IdempotencyKey.Status.COMPLETED)
                .map(record ->
                        ResponseEntity
                                .status(record.getResponseStatus())
                                .body(record.getResponseBody())
                );
    }

    @Transactional
    public void saveProcessingKey(String key, String endpoint, String userId) {
        IdempotencyKey record = new IdempotencyKey();
        record.setIdempotencyKey(key);
        record.setEndpoint(endpoint);
        record.setUserId(userId);
        record.setStatus(IdempotencyKey.Status.PROCESSING);
        record.setCreatedAt(LocalDateTime.now());
        repository.save(record);
    }

    @Transactional
    public void completeKey(String key, ResponseEntity<String> response) {
        IdempotencyKey record = repository.findById(key).orElseThrow();
        record.setStatus(IdempotencyKey.Status.COMPLETED);
        record.setResponseBody(response.getBody());
        record.setResponseStatus(response.getStatusCodeValue());
    }
}