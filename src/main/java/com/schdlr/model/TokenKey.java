package com.schdlr.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Entity representing the token keys (public and private) for JWT operations.
 * 
 * Annotations:
 * - @Entity: Marks this class as a JPA entity.
 * - @Data: Lombok annotation to generate getters, setters, and other utility methods.
 * - @AllArgsConstructor: Lombok annotation to generate a constructor with all fields.
 * - @NoArgsConstructor: Lombok annotation to generate a no-arguments constructor.
 * - @CreationTimestamp: Automatically sets the creation time.
 * 
 * Fields:
 * - `kid`: Unique identifier for the key pair.
 * - `publicKey`: Public key used for JWT signing.
 * - `privateKey`: Private key used for JWT signing.
 * - `keyActivity`: Enum indicating the activity status of the key (ACTIVE or GRACE).
 * - `timeOfCreation`: Timestamp indicating when the key pair was created.
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenKey {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_key_sequence")
    @SequenceGenerator(name = "token_key_sequence", sequenceName = "token_key_sequence")
    private Long kid;

    @Column(nullable = false, unique = true, length = 5000) 
    private String publicKey;

    @Column(nullable = false, unique = true, length = 5000) 
    private String privateKey;

    @Enumerated(EnumType.STRING)
    private KeyActivity keyActivity;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant timeOfCreation;
}
