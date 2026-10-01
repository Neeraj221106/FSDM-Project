package com.ztplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class AuditLogDTO {

    private Long id;
    private String action;
    private String description;

    @JsonProperty("ip_address")
    private String ipAddress;

    private LocalDateTime timestamp;

    public AuditLogDTO() {
    }

    public AuditLogDTO(
            Long id,
            String action,
            String description,
            String ipAddress,
            LocalDateTime timestamp
    ) {
        this.id = id;
        this.action = action;
        this.description = description;
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
