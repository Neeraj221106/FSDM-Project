package com.ztplatform.dto;

import com.ztplatform.model.Credential;
import com.ztplatform.model.Document;

import java.time.LocalDateTime;

public class CredentialDTO {

    private Long id;
    private String credentialId;
    private Long documentId;
    private String digitalSignature;
    private String qrData;
    private Credential.CredentialStatus status;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime revokedAt;

    // Document details for frontend
    private Document.DocumentType documentType;
    private String documentTitle;
    private String fileUrl;

    // Student details for admin
    private String studentName;

    public CredentialDTO() {
    }

    public CredentialDTO(
            Long id,
            String credentialId,
            Long documentId,
            String digitalSignature,
            String qrData,
            Credential.CredentialStatus status,
            LocalDateTime issuedAt,
            LocalDateTime expiresAt,
            LocalDateTime revokedAt
    ) {
        this.id = id;
        this.credentialId = credentialId;
        this.documentId = documentId;
        this.digitalSignature = digitalSignature;
        this.qrData = qrData;
        this.status = status;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public String getQrData() {
        return qrData;
    }

    public void setQrData(String qrData) {
        this.qrData = qrData;
    }

    public Credential.CredentialStatus getStatus() {
        return status;
    }

    public void setStatus(Credential.CredentialStatus status) {
        this.status = status;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    public Document.DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Document.DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}