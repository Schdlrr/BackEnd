package com.schdlr.repo;

import com.schdlr.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserManagmentRepo extends JpaRepository<User,Integer>{



}
