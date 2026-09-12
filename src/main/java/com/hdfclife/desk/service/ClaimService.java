package com.hdfclife.desk.service;

import com.hdfclife.desk.config.HdfcProperties;
import com.hdfclife.desk.exception.ClaimNotFoundException;
import com.hdfclife.desk.exception.InvalidClaimException;
import com.hdfclife.desk.exception.PolicyNotFoundException;
import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.store.InMemoryPolicyStore;
import com.hdfclife.desk.store.PolicyStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClaimService {

    private final PolicyStore policyStore;
    private final HdfcProperties hdfcProperties;

    public ClaimService(InMemoryPolicyStore store, HdfcProperties props) {
        this.policyStore = store;
        this.hdfcProperties = props;
    }

    public Claim createClaim(Claim claim) {

        if (claim.getClaimAmount() <= 0 || claim.getClaimAmount() > hdfcProperties.getMaxClaimAmount()) {

            throw new InvalidClaimException("Claim amount invalid");
        }

        Claim createdClaim = policyStore.addClaim(claim);

        if(createdClaim == null) {

            throw new PolicyNotFoundException("Policy No. " +  claim.getPolicyNo() + " does not exist.");
        }

        return claim;
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
