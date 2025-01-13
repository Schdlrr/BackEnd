package com.schdlr.repo;

import com.schdlr.model.SignedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SignedUserRepo extends JpaRepository<SignedUser,Integer>{

    Optional<SignedUser> findByUserName(String userName);

    Optional<SignedUser> findByEmail(String email);


}
