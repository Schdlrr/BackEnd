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
public class BussinessOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bussines_owner_sequence")
    @SequenceGenerator(name = "bussines_owner_sequence", sequenceName = "bussines_owner_seq")
    private Integer id;
    private String userName;
    private String email;
    private String number;
    private String address;
    private String bussinessName;


}
