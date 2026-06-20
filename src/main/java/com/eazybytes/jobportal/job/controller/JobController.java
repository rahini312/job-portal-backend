package com.eazybytes.jobportal.job.controller;

import com.eazybytes.jobportal.dto.JobApplicationDto;
import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.dto.UpdateJobApplicationDto;
import com.eazybytes.jobportal.job.service.IJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor

public class JobController {
    private final IJobService jobService;

    @GetMapping(value ="/employer", version = "1.0")
    public ResponseEntity<List<JobDto>> getEmployerJobs(Authentication authentication) {
        String email = authentication.getName();
        List<JobDto> jobDtos = jobService.getEmployerJobs(email);
        return ResponseEntity.ok(jobDtos);
    }

    @PostMapping(value = "/employer", version = "1.0")
    public ResponseEntity<JobDto> postJob(@RequestBody @Valid JobDto jobDto, Authentication authentication) {
        String mail = authentication.getName();
        JobDto createdJob = jobService.createJob(jobDto, mail);
        return ResponseEntity.status(HttpStatus.OK).body(createdJob);
    }

    @PatchMapping(value ="/{jobId}/status/employer", version = "1.0")
    public ResponseEntity<?> updateJobStatus(@PathVariable Long jobId,
                                             @RequestBody Map<String, String> requestBody, Authentication authentication) {
        String status = requestBody.get("status");
        if(status == null || status.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request, status cant be null");
        }

        String email = authentication.getName();
        JobDto updatedJob = jobService.updateJobStatus(jobId, status.toUpperCase(), email);
        return ResponseEntity.ok(updatedJob);
    }

    @GetMapping("/applications/{jobId}/employer")
    public ResponseEntity<List<JobApplicationDto>> getApplicationsByJobForEmployer(
            @PathVariable Long jobId) {
        List<JobApplicationDto> applications = jobService.getApplicationsByJobForEmployer(jobId);
        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/applications/employer")
    public ResponseEntity<String> updateJobApplication(
            @RequestBody @Valid UpdateJobApplicationDto updateJobApplicationDto) {
        boolean isUpdated = jobService.updateJobApplication(updateJobApplicationDto);
        if(!isUpdated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update application");
        }
        return ResponseEntity.ok("Application updated successfully");
    }
}
