package com.ztplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecideRequestDTO {

    private String action;

    @JsonProperty("rejection_reason")
    private String rejectionReason;

    public DecideRequestDTO() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
