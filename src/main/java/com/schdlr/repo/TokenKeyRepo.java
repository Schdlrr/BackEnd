package com.schdlr.repo;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.schdlr.model.KeyActivity;
import com.schdlr.model.TokenKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenKeyRepo extends JpaRepository <TokenKey,Integer> {


    @Modifying
    @Query("DELETE FROM TokenKey t WHERE t.kid <= :kid")
    void deleteOldKeys(@Param("kid") Long kid);

    @Query("SELECT t FROM TokenKey t WHERE t.keyActivity = 'ACTIVE'")
    List<TokenKey> findAllActiveKeys();

    @Query("SELECT t FROM TokenKey t WHERE t.kid = :kid")
    Optional<TokenKey> findById(Long kid);
}
