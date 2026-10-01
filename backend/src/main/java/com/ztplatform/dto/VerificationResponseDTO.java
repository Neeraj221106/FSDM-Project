package com.ztplatform.dto;

import java.time.LocalDateTime;

public class VerificationResponseDTO {

    private String credentialId;
    private String studentId;
    private String studentName;
    private String documentType;
    private String title;
    private String status;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private String fileUrl;
    private String issuedBy;

    public VerificationResponseDTO() {
    }

    public VerificationResponseDTO(
            String credentialId,
            String studentId,
            String studentName,
            String documentType,
            String title,
            String status,
            LocalDateTime issuedAt,
            LocalDateTime expiresAt,
            String fileUrl,
            String issuedBy
    ) {
        this.credentialId = credentialId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.documentType = documentType;
        this.title = title;
        this.status = status;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.fileUrl = fileUrl;
        this.issuedBy = issuedBy;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }
}