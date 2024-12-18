package com.schdlr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
/*
 * Entity representing a signed-up user in the system.
 * 
 * Annotations:
 * - @Entity: Marks this class as a JPA entity.
 * - @Data: Lombok annotation to generate getters, setters, and other utility methods.
 * - @AllArgsConstructor: Lombok annotation to generate a constructor with all fields.
 * - @NoArgsConstructor: Lombok annotation to generate a no-arguments constructor.
 * 
 * Fields:
 * - `id`: Unique identifier for the signed-up user (auto-generated).
 * - `userName`: Name of the user.
 * - `email`: Email address of the user.
 * - `number`: Contact number of the user.
 * - `password`: Encrypted password for the user.
 */
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignedUser{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "signedUpUser_seq")
    @SequenceGenerator(name = "signedUpUser_seq", sequenceName = "signed_up_user_sequence", allocationSize = 1)
    private Integer id;

    private String userName;

    private String email;

    private String number;

    private String password;

}
