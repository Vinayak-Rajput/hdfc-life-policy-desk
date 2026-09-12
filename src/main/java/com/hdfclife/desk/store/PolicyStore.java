package com.hdfclife.desk.store;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;

import java.util.List;

public interface PolicyStore {

    Policy add(Policy policy);
    List<Policy> findAll();
    Policy findByPolicyNo(String policyNo);
    Policy update(String policyNo, Policy policy);
    Policy delete(String policyNo);
    int count();

    Claim addClaim(Claim claim);
    List<Claim> findAllClaims();
    Claim findClaimByClaimNo(String claimNo);
    List<Claim> findClaimsByPolicyNo(String claimNo);
    int claimCount();

}
