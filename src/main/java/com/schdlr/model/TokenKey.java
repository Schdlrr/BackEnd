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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
