package com.schdlr.repo;

import java.util.List;
import java.util.Optional;

import com.schdlr.model.TokenKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface TokenKeyRepo extends JpaRepository <TokenKey,Integer> {


    /*
     * Custom query to delete old keys based on their `kid`.
     * 
     * kid - Threshold ID; keys with `kid` equal to this will be deleted.
     * 
     * Annotations:
     * - @Modifying: Indicates that the query modifies data (delete operation).
     * - @Query: Custom JPQL query for deletion.
     */

    @Modifying
    @Transactional
    @Query("DELETE FROM TokenKey t WHERE t.kid = :kid")
    void deleteKeyById(@Param("kid") Long kid);

    /*
     * Finds all active keys with a status of 'ACTIVE'.
     * 
     * returns a List of active `TokenKey` entities.
     * 
     * Annotation:
     * - @Query: Custom JPQL query to filter by `keyActivity`.
     */
    @Query("SELECT t FROM TokenKey t WHERE t.keyActivity = 'ACTIVE'")
    List<TokenKey> findAllActiveKeys();


    /*
     * Finds a `TokenKey` entity by its `kid`.
     * 
     * kid - Unique identifier of the key.
     * returns An `Optional` containing the found `TokenKey`, or empty if not found.
     * 
     * Annotation:
     * - @Query: Custom JPQL query to fetch by `kid`.
     */
    @Query("SELECT t FROM TokenKey t WHERE t.kid = :kid")
    Optional<TokenKey> findById(Long kid);
}
