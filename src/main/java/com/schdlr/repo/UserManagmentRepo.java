package com.schdlr.repo;

import java.util.Optional;

import com.schdlr.model.SignedUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserManagmentRepo extends JpaRepository<SignedUser,Integer>{

    Optional<SignedUser> findByUserName(String userName);

    Optional<SignedUser> findByEmail(String email);


}
