package com.hdfclife.desk.web;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.service.ClaimService;
import com.hdfclife.desk.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@Tag(name = "Policy APIs", description = "APIs endpoints related to Creating, Fetching, Updating and Deleting Policies")
public class PolicyController {

    PolicyService policyService;
    ClaimService claimService;

    public PolicyController(PolicyService policyService, ClaimService claimService) {
        this.policyService = policyService;
        this.claimService = claimService;
    }

    @GetMapping
    @Operation(summary = "Get All Policies", description = "Fetch all Policy resources")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved all policies",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Policy.class)
            )
    )
    public ResponseEntity<List<Policy>> getPolicies(
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by status") @RequestParam(required = false) String type) {

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
    @Operation(summary = "Get Policy by No", description = "Fetch Policy resource by Policy No.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved Policy",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Policy.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Policy not found",
                    content = @Content
            )
    })
    public ResponseEntity<Policy> getPolicyByNo(@PathVariable("policyNo") String policyNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.getPolicyByNo(policyNo));
    }

    @PostMapping
    @Operation(
            summary = "Create a Policy",
            description = "Create a new Policy resource.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload to create an item",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Policy.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a Policy",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Policy.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Duplicate Policy Conflict",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Update a Policy",
            description = "Update an existing Policy resource.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload to update item",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Policy.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated a Policy",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Policy.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Policy doesn't exist",
                    content = @Content
            )
    })
    public ResponseEntity<Policy> updatePolicy(@PathVariable("policyNo") String policyNo, @RequestBody Policy policy) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(policyService.updatePolicy(policyNo, policy));
    }

    @DeleteMapping("/{policyNo}")
    @Operation(summary = "Delete a Policy", description = "Delete an existing Policy resource.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted a Policy",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Policy.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Policy doesn't exist",
                    content = @Content
            )
    })
    public ResponseEntity<Policy> deletePolicy(@PathVariable("policyNo") String policyNo) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(policyService.deletePolicy(policyNo));
    }

    @GetMapping("/{policyNo}/claims")
    @Operation(summary = "Get Claims by PolicyNo", description = "Fetch Claim resources filed on Policy No.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved Claims",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Claim.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Claims not found",
                    content = @Content
            )
    })
    public ResponseEntity<List<Claim>> getClaimsByNo(@PathVariable("policyNo") String policyNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(claimService.getClaimsByPolicyNo(policyNo));
    }
}
