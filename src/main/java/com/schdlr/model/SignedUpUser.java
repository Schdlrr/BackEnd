package com.schdlr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SignedUpUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "signed_up_user_sequence")
    @SequenceGenerator(name = "signed_up_user_sequence", sequenceName = "signed_up_user_seq")
    private Integer id;
    private String userName;
    private String email;
    private String number;
    private String password;
}
