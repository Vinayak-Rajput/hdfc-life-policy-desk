package com.hdfclife.desk.service;

import com.hdfclife.desk.exception.DuplicatePolicyException;
import com.hdfclife.desk.exception.PolicyNotFoundException;
import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.store.PolicyStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolicyService {

    PolicyStore policyStore;

    public PolicyService(PolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    public Policy createPolicy(Policy policy) {

        Policy createdPolicy = policyStore.add(policy);

        if(createdPolicy == null){

            throw new DuplicatePolicyException("Policy already exists");
        }

        return createdPolicy;
    }

    public Policy getPolicyByNo(String policyNo) {

        Policy policy = policyStore.findByPolicyNo(policyNo);

        if(policy == null) {

            throw new PolicyNotFoundException("Policy with No. " + policyNo + " doesn't exist.");
        }
        return policy;
    }

    public Policy updatePolicy(String policyNo, Policy policy) {

        if(policyStore.findByPolicyNo(policyNo) == null) {

            throw new PolicyNotFoundException("Policy with No. " + policyNo + " doesn't exist.");
        }

        Policy updatedPolicy = policyStore.update(policyNo, policy);
        return policy;

    }

    public Policy deletePolicy(String policyNo) {

        Policy policy = policyStore.findByPolicyNo(policyNo);

        if(policy == null) {

            throw new PolicyNotFoundException("Policy with No. " + policyNo + " doesn't exist.");
        }

        policyStore.delete(policyNo);

        return policy;
    }

    public List<Policy> getPoliciesByStatus(String status) {

        return policyStore.findAll()
                .stream()
                .filter(p -> p.getStatus().equals(status))
                .toList();
    }

    public List<Policy> getPoliciesByType(String type) {

        return policyStore.findAll()
                .stream()
                .filter(p -> p.getType().equals(type))
                .toList();
    }

    public List<Policy> getPolicies() {

        return policyStore.findAll();
    }
}
