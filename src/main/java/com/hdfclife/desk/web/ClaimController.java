package com.hdfclife.desk.web;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.service.ClaimService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    ClaimService claimService;
    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    public ResponseEntity<Claim> createClaim(@RequestBody Claim claim) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(claim);
    }

    @GetMapping("/{claimNo}")
    public ResponseEntity<Claim> getClaimByNo(@PathVariable String claimNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(claimService.getClaimByNo(claimNo));
    }
}
