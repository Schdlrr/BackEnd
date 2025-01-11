package com.schdlr.repo;

import com.schdlr.model.TokenKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenKeyRepo extends JpaRepository <TokenKey,Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM TokenKey t WHERE t.kid = :kid")
    void deleteKeyById(@Param("kid") Long kid);

    @Query("SELECT t FROM TokenKey t WHERE t.keyActivity = 'ACTIVE'")
    List<TokenKey> findAllActiveKeys();

    @Query("SELECT t FROM TokenKey t WHERE t.kid = :kid")
    Optional<TokenKey> findById(@Param("kid") Long kid);
}
