package com.hdfclife.desk.store;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;

import java.util.List;
import java.util.Optional;

public interface PolicyStore {

    void add(Policy policy);
    List<Policy> findAll();
    Optional<Policy> findByPolicyNo(String policyNo);
    boolean update(String policyNo, Policy policy);
    boolean delete(String policyNo);
    int count();

    void addClaim(Claim claim);
    Optional<Claim> findClaimByClaimNo(String claimNo);
    List<Claim> findClaimsByPolicyNo(String claimNo);
    int claimCount();

}
