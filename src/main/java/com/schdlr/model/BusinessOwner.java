package com.schdlr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessOwner{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "businessOwnerSeq")
    @SequenceGenerator(name = "businessOwnerSeq", sequenceName = "business_owner_sequence", allocationSize = 1)
    private Integer id;

    private String userName;

    private String email;

    private String number;

    private String password;

    private String businessAddress;
    
    private String businessName;


}
