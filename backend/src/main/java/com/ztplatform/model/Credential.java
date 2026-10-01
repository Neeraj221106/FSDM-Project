package com.ztplatform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "credentials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String credentialId;

    @Column(name = "credential_name", nullable = false)
    private String credentialName;

    @OneToOne
    @JoinColumn(name = "document_id", nullable = false, unique = true)
    private Document document;

    @Column(nullable = false, length = 1000)
    private String digitalSignature;

    @Column(nullable = false, length = 2000)
    private String qrData;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CredentialStatus status;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    private LocalDateTime expiresAt;

    private LocalDateTime revokedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        issuedAt = LocalDateTime.now();
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = CredentialStatus.VALID;
        }
    }

    public enum CredentialStatus {
        VALID,
        REVOKED,
        EXPIRED
    }
}