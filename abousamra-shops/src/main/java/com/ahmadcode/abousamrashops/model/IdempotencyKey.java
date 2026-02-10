package com.ahmadcode.abousamrashops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_keys")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyKey {

    @Id
    @Column(name = "idempotency_key")
    private String idempotencyKey;

    private String endpoint;
    private String userId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Lob
    private String responseBody;

    private int responseStatus;

    private LocalDateTime createdAt;



    public enum Status {
        PROCESSING,
        COMPLETED
    }
}