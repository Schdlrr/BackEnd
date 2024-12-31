/* package com.schdlr.model;

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
public class OAuth2User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oAuthUserSeq")
    @SequenceGenerator(name = "oAuth2Seq", sequenceName = "oAuth_user_sequence", allocationSize = 1)
    private Integer id;

    private String email;



} */
