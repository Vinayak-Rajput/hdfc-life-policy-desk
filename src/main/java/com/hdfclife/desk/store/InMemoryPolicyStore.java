package com.hdfclife.desk.store;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryPolicyStore implements PolicyStore {

    private final Map<String, Policy> policies = new HashMap<>();
    private final List<String> insertionOrder = new ArrayList<>();

    private final Map<String, Claim> claims = new HashMap<>();
    private final List<Claim> claimOrder = new ArrayList<>();

    @Override
    public Policy add(Policy policy) {

        if(!policies.containsKey(policy.getPolicyNo())) {

            insertionOrder.add(policy.getPolicyNo());
        }

        policies.put(policy.getPolicyNo(), policy);

        return policy;
    }

    @Override
    public List<Policy> findAll() {
        List<Policy> policyList = new ArrayList<>();
        for(String policyNo : insertionOrder) {
            if(policies.containsKey(policyNo)) {
                policyList.add(policies.get(policyNo));
            }
        }
        return policyList;
    }

    @Override
    public Policy findByPolicyNo(String policyNo) {
        return policies.get(policyNo);
    }

    @Override
    public Policy update(String policyNo, Policy policy) {
        if(!policies.containsKey(policy.getPolicyNo())) {
            return null;
        }
        policies.put(policy.getPolicyNo(), policy);
        return policy;
    }

    @Override
    public Policy delete(String policyNo) {

        if(policies.containsKey(policyNo)) {

            Policy deletedPolicy = policies.remove(policyNo);
            insertionOrder.remove(policyNo);

            return deletedPolicy;
        }
        return null;
    }

    @Override
    public int count() {
        return policies.size();
    }

    @Override
    public Claim addClaim(Claim claim) {
        claims.put(claim.getPolicyNo(),claim);
        claimOrder.add(claim);

        return claim;
    }

    @Override
    public List<Claim> findAllClaims() {
        return new ArrayList<>(claims.values());
    }

    @Override
    public Claim findClaimByClaimNo(String claimNo) {
        return claims.get(claimNo);
    }

    @Override
    public List<Claim> findClaimsByPolicyNo(String policyNo) {
        return claimOrder.stream()
                .filter(claim -> claim.getPolicyNo().equals(policyNo))
                .collect(Collectors.toList());
    }

    @Override
    public int claimCount() {
        return claims.size();
    }
}
