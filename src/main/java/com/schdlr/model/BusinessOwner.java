package com.schdlr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Entity representing a Business Owner in the system.
 * 
 * Annotations:
 * - @Entity: Marks this class as a JPA entity.
 * - @Data: Lombok annotation to generate getters, setters, and other utility methods.
 * - @AllArgsConstructor: Lombok annotation to generate a constructor with all fields.
 * - @NoArgsConstructor: Lombok annotation to generate a no-arguments constructor.
 * 
 * Fields:
 * - `id`: Unique identifier for the business owner (auto-generated).
 * - `userName`: Name of the user.
 * - `email`: Email address of the user.
 * - `number`: Contact number of the user.
 * - `address`: Address of the business.
 * - `bussinessName`: Name of the business owned by the user.
 */

@Entity
@Data
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
