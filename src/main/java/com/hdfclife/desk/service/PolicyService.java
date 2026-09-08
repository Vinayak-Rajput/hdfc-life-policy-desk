package com.hdfclife.desk.service;

import com.hdfclife.desk.store.PolicyStore;
import org.springframework.stereotype.Service;

@Service
public class PolicyService {

    PolicyStore policyStore;

    public PolicyService(PolicyStore policyStore) {
        this.policyStore = policyStore;
    }
}
