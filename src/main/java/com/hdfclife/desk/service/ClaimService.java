package com.hdfclife.desk.service;

import com.hdfclife.desk.exception.ClaimNotFoundException;
import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.store.InMemoryPolicyStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClaimService {

    private final InMemoryPolicyStore policyStore;

    public ClaimService() {
        this.policyStore = new InMemoryPolicyStore();
    }

    public Claim createClaim(Claim claim) {

        return policyStore.addClaim(claim);
    }
    public Claim getClaimByNo(String claimNo) {

        Claim claim = policyStore.findClaimByClaimNo(claimNo);

        if(claim == null) {

            throw new ClaimNotFoundException("Claim with No. " + claimNo + " doesn't exist.");
        }

        return claim;
    }

    public List<Claim> getClaimsByPolicyNo(String policyNo) {

        List<Claim> claims = policyStore.findAllClaims();

        List<Claim> filteredClaims = new ArrayList<>();

        for(Claim claim : claims){

            if(claim.getPolicyNo().equals(policyNo)){

                filteredClaims.add(claim);
            }
        }

        return filteredClaims;
    }
}
