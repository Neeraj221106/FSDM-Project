package com.ztplatform.repository;

import com.ztplatform.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Optional<Credential> findByCredentialId(String credentialId);

    boolean existsByCredentialId(String credentialId);

    Optional<Credential> findByDocumentId(Long documentId);

    long countByStatus(Credential.CredentialStatus status);

    @Query("SELECT c FROM Credential c JOIN c.document d JOIN d.student s JOIN s.user u WHERE u.email = :userEmail")
    List<Credential> findByUserEmail(String userEmail);
}