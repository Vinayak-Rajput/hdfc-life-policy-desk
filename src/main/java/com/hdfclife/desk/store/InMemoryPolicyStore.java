package com.hdfclife.desk.store;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryPolicyStore implements PolicyStore {

    private final Map<String, Policy> policies = new HashMap<>();
    private final List<String> insertionOrder = new ArrayList<>();

    private final Map<String, List<Claim>> claims = new HashMap<>();
    private final List<Claim> claimOrder = new ArrayList<>();
    private int claimCounter = 1;

    @Override
    public Policy add(Policy policy) {

        String policyNo = policy.getPolicyNo();

        if(policies.containsKey(policyNo)) {

            return null;
        }

        insertionOrder.add(policyNo);
        policies.put(policyNo, policy);

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
        String policyNo = claim.getPolicyNo();

        if(!policies.containsKey(policyNo)) {

            return null;
        }

        String claimNo = String.format("CLM-%02d",claimCounter++);

        claim.setClaimNo(claimNo);

        claims.computeIfAbsent(policyNo, k -> new ArrayList<>()).add(claim);

        claimOrder.add(claim);

        return claim;
    }

    @Override
    public List<Claim> findAllClaims() {
        return claimOrder;
    }

    @Override
    public Claim findClaimByClaimNo(String claimNo) {

        for(Claim claim : claimOrder) {

            if(claim.getClaimNo().equals(claimNo)) {

                return claim;
            }
        }
        return null;
    }

    @Override
    public List<Claim> findClaimsByPolicyNo(String policyNo) {

        return claims.get(policyNo);
    }

    @Override
    public int claimCount() {
        return claimOrder.size();
    }
}
