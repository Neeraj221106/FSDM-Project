package com.ztplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ztplatform.model.Document;

import java.time.LocalDateTime;

public class RequestDTO {

    private Long id;
    private Long studentId;
    private Document.DocumentType documentType;
    private String reason;
    private String status;
    private String rejectionReason;
    private Long reviewedBy;
    @JsonProperty("doc_type")
    private String docType;
    private String purpose;
    private String remarks;
    private LocalDateTime requestedAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("student_name")
    private String studentName;

    @JsonProperty("roll_no")
    private String rollNo;

    private String course;
    private Integer year;

    public RequestDTO() {
    }

    public RequestDTO(
            Long id,
            Long studentId,
            Document.DocumentType documentType,
            String reason,
            String status,
            String rejectionReason,
            Long reviewedBy
    ) {
        this.id = id;
        this.studentId = studentId;
        this.documentType = documentType;
        this.reason = reason;
        this.status = status;
        this.rejectionReason = rejectionReason;
        this.reviewedBy = reviewedBy;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Long getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Long reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}