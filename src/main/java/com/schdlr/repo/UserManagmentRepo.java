package com.schdlr.repo;

import java.util.Optional;

import com.schdlr.model.SchdlrUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagmentRepo extends JpaRepository<SchdlrUser,Integer>{

    Optional<SchdlrUser> findByUserName(String userName);

    SchdlrUser findByPassword(String password);



}
