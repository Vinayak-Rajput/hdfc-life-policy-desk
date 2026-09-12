package com.hdfclife.desk.web;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.service.ClaimService;
import com.hdfclife.desk.service.PolicyService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    PolicyService policyService;
    ClaimService claimService;

    public PolicyController(PolicyService policyService, ClaimService claimService) {
        this.policyService = policyService;
        this.claimService = claimService;
    }

    @GetMapping
    public ResponseEntity<List<Policy>> getPolicies(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type ) {

            if (status != null && type != null) {

                return ResponseEntity.ok(
                        policyService.getPolicies().stream()
                                .filter(p -> p.getStatus().equals(status))
                                .filter(p -> p.getType().equals(type))
                                .toList()
                );
            }

            if (status != null) {
                return ResponseEntity.ok(policyService.getPoliciesByStatus(status));
            }

            if (type != null) {
                return ResponseEntity.ok(policyService.getPoliciesByType(type));
            }

            return ResponseEntity.ok(policyService.getPolicies());
    }

    @GetMapping("/{policyNo}")
    public ResponseEntity<Policy> getPolicyByNo(@PathVariable("policyNo") String policyNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.getPolicyByNo(policyNo));
    }

    @PostMapping
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy policy) {
        Policy createdPolicy = policyService.createPolicy(policy);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/policies/" + createdPolicy.getPolicyNo());

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(createdPolicy);
    }

    @PutMapping("/{policyNo}")
    public ResponseEntity<Policy> updatePolicy(@PathVariable("policyNo") String policyNo, @RequestBody Policy policy) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.updatePolicy(policyNo, policy));
    }

    @DeleteMapping("/{policyNo}")
    public ResponseEntity<Policy> deletePolicy(@PathVariable("policyNo") String policyNo) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(policyService.deletePolicy(policyNo));
    }

    @GetMapping("/{policyNo}/claims")
    public ResponseEntity<List<Claim>> getClaimsByNo(@PathVariable("policyNo") String policyNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(claimService.getClaimsByPolicyNo(policyNo));
    }
}
