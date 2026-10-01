package com.ztplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminStatsDTO {

    @JsonProperty("total_students")
    private long totalStudents;

    @JsonProperty("total_documents")
    private long totalDocuments;

    @JsonProperty("pending_requests")
    private long pendingRequests;

    @JsonProperty("revoked")
    private long revoked;

    public AdminStatsDTO() {
    }

    public AdminStatsDTO(
            long totalStudents,
            long totalDocuments,
            long pendingRequests,
            long revoked
    ) {
        this.totalStudents = totalStudents;
        this.totalDocuments = totalDocuments;
        this.pendingRequests = pendingRequests;
        this.revoked = revoked;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(long totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(long pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public long getRevoked() {
        return revoked;
    }

    public void setRevoked(long revoked) {
        this.revoked = revoked;
    }
}
