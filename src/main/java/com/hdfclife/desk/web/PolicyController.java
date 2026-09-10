package com.hdfclife.desk.web;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.service.PolicyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    public ResponseEntity<List<Policy>> getPolicies() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ArrayList<>());
    }

    @GetMapping("/{policyNo}")
    public ResponseEntity<Policy> getPolicyByNo(@PathVariable("policyNo") String policyNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.getPolicyByNo(policyNo));
    }

    @GetMapping("?status=Active")
    public ResponseEntity<List<Policy>> getPoliciesByStatus(@RequestParam("status") String status) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.getPoliciesByStatus(status));
    }

    @GetMapping("?type=TERM")
    public ResponseEntity<List<Policy>> getPoliciesByType(@RequestParam("type") String type) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.getPoliciesByType(type));
    }

    @PostMapping
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy policy) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.createPolicy(policy));
    }

    @PutMapping("/{policyNo}")
    public ResponseEntity<Policy> updatePolicy(@PathVariable("policyNo") String policyNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.updatePolicy(policyNo));
    }

    @DeleteMapping("/{policyNo}")
    public ResponseEntity<Policy> deletePolicy(@PathVariable("policyNo") String policyNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.deletePolicy(policyNo));
    }

    @GetMapping("/{policyNo}/claims")
    public ResponseEntity<List<Claim>> getClaimsByNo(@PathVariable("policyNo") String policyNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.getClaimsByPolicyNo(policyNo));
    }
}
