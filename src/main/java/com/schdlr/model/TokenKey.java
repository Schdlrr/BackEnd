package com.schdlr.model;

import com.schdlr.dto.KeyActivity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
