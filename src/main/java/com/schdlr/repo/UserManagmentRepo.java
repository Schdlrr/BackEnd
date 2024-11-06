package com.schdlr.repo;

import java.util.Optional;

import com.schdlr.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagmentRepo extends JpaRepository<User,Integer>{

    Optional<User> findByUsername(String username);

    User findByPassword(String password);



}
