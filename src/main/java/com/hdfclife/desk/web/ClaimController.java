package com.hdfclife.desk.web;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.service.ClaimService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claims")
@Tag(name = "Claim APIs", description = "APIs endpoints related to Filling & Fetching Claims")
public class ClaimController {

    ClaimService claimService;
    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    @Operation(
            summary = "Create a Claim / File a Claim",
            description = "Create a new Claim resource.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload to create an item",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Claim.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a Claim",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Claim.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid Claim Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Policy Not Found for Claim to be filed.",
                    content = @Content
            )
    })
    public ResponseEntity<Claim> createClaim(@RequestBody Claim claim) {
        Claim createdClaim = claimService.createClaim(claim);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/claims/" + createdClaim.getClaimNo());

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(createdClaim);
    }

    @GetMapping("/{claimNo}")
    @Operation(summary = "Get Claim by Claim No", description = "Fetch Claim resource by Claim No.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved Claim",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Claim.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Claim not found",
                    content = @Content
            )
    })
    public ResponseEntity<Claim> getClaimByNo(@PathVariable String claimNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(claimService.getClaimByNo(claimNo));
    }
}
