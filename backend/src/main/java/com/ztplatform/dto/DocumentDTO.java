package com.ztplatform.dto;

import com.ztplatform.model.Document;

public class DocumentDTO {

    private Long id;
    private Long studentId;
    private Document.DocumentType documentType;
    private String title;
    private String fileUrl;
    private Document.DocumentStatus status;

    public DocumentDTO() {
    }

    public DocumentDTO(
            Long id,
            Long studentId,
            Document.DocumentType documentType,
            String title,
            String fileUrl,
            Document.DocumentStatus status
    ) {
        this.id = id;
        this.studentId = studentId;
        this.documentType = documentType;
        this.title = title;
        this.fileUrl = fileUrl;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Document.DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Document.DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Document.DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(Document.DocumentStatus status) {
        this.status = status;
    }
}