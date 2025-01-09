package com.schdlr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignedUser{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "signedUserSeq")
    @SequenceGenerator(name = "signedUpUserSeq", sequenceName = "signed_user_sequence", allocationSize = 1)
    private Integer id;

    private String userName;

    private String email;

    private String number;

    private String password;

}
