package com.schdlr.repo;

import java.util.Optional;

import com.schdlr.model.SignedUpUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserManagmentRepo extends JpaRepository<SignedUpUser,Integer>{


    /*
     * Finds a `SignedUpUser` by their username.
     * 
     * userName - The username of the user.
     * returns An `Optional` containing the found user, or empty if not found.
     */
    Optional<SignedUpUser> findByUserName(String userName);

    /*
     * Finds a `SignedUpUser` by their email address.
     * 
     * email - The email address of the user.
     * returns An `Optional` containing the found user, or empty if not found.
     */
    Optional<SignedUpUser> findByEmail(String email);


}
