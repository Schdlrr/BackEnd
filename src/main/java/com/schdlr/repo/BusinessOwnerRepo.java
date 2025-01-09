package com.schdlr.repo;

import java.util.Optional;

import com.schdlr.model.BusinessOwner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessOwnerRepo extends JpaRepository<BusinessOwner, Integer> {

    Optional<BusinessOwner> findByUserName(String userName);

    Optional<BusinessOwner> findByEmail(String email);

}
