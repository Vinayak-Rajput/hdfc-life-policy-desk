package com.hdfclife.desk.model;

public class Claim {

    private String claimNo;
    private final String policyNo;
    private final Double claimAmount;
    private final Urgency urgency;
    private String status;

    public Claim(String claimNo,String policyNo, Double claimAmount, Urgency urgency, String status) {
        this.claimNo = claimNo;
        this.policyNo = policyNo;
        this.claimAmount = claimAmount;
        this.urgency = urgency;
        this.status = status;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public Double getClaimAmount() {
        return claimAmount;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public String getStatus() {
        return status;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}