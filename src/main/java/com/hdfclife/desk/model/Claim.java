package com.hdfclife.desk.model;

public class Claim {

    private String policyNo;
    private Double claimAmount;
    private Urgency urgency;

    public Claim(String policyNo, Double claimAmount, Urgency urgency) {
        this.policyNo = policyNo;
        this.claimAmount = claimAmount;
        this.urgency = urgency;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public Double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(Double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }
}
