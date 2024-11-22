package com.schdlr.repo;

import java.util.Optional;

import com.schdlr.model.SignedUpUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserManagmentRepo extends JpaRepository<SignedUpUser,Integer>{

    Optional<SignedUpUser> findByUserName(String userName);

    Optional<SignedUpUser> findByContactInfo(String contactInfo);


}
