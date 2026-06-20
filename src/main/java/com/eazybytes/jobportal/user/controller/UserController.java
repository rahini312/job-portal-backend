package com.eazybytes.jobportal.user.controller;

import com.eazybytes.jobportal.dto.*;
import com.eazybytes.jobportal.user.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {
    private final IUserService userService;

    @GetMapping("/search/admin")
    public ResponseEntity<?> searchUserByEmail(@RequestParam String email)
    {
        Optional<UserDto> userOptional = userService.searchUserByEmail(email);
        if(userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "User not found with email " + email));
        return ResponseEntity.ok(userOptional.get());
    }

    @PatchMapping("/{userId}/role/employer/admin")
    public ResponseEntity<UserDto> elevateUserToEmployee(@PathVariable Long userId)
    {
        UserDto user = userService.elevateToEmployer(userId);
        return ResponseEntity.ok(user);
    }
    @PatchMapping("/{userId}/company/{companyId}/admin")
    public ResponseEntity<?> assignCompanyToEmployer(
            @PathVariable Long userId, @PathVariable Long companyId) {
        UserDto updatedUser = userService.assignCompanyToEmployer(userId, companyId);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping(path = "/profile/jobseeker", version = "1.0",
    //Since resume, profile pic will be uploaded we are using mediatype
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileDto> updateUserProfile(
            @RequestPart(value = "profile")  String profileJson,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestPart(value = "resume", required = false) MultipartFile resume,
            Authentication authentication)throws JsonProcessingException
    {
            String username = authentication.getName();
            ProfileDto dto =userService.createOrUpdateProfile(username, profileJson, profilePicture, resume);
            return ResponseEntity.ok(dto);
    }

    @GetMapping(path = "/profile/jobseeker", version = "1.0")
    public ResponseEntity<ProfileDto> getUserProfile(Authentication authentication)
    {
        String username = authentication.getName();
        ProfileDto dto = userService.getProfile(username);
        return ResponseEntity.ok(dto);
    }
    @GetMapping(value = "/profile/picture/jobseeker", version = "1.0")
    public ResponseEntity<byte[]> getProfilePicture(Authentication authentication) {
        String userEmail = authentication.getName();
        com.eazybytes.jobportal.dto.ProfileDto profileDto = userService.getProfilePicture(userEmail);
        byte[] picture = profileDto.profilePicture();
        if (picture == null || picture.length == 0) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profileDto.profilePictureType()));
        headers.setContentLength(picture.length);
        return new ResponseEntity<>(picture, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/profile/resume/jobseeker", version = "1.0")
    public ResponseEntity<byte[]> getResume(Authentication authentication) {
        String userEmail = authentication.getName();
        com.eazybytes.jobportal.dto.ProfileDto profileDto = userService.getResume(userEmail);
        byte[] resume = profileDto.resume();
        if (resume == null || resume.length == 0) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profileDto.resumeType()));
        headers.setContentLength(resume.length);
        headers.setContentDispositionFormData("attachment", profileDto.resumeName());
        return new ResponseEntity<>(resume, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/saved-jobs/{jobId}/jobseeker", version = "1.0")
    public ResponseEntity<JobDto> saveJob(@PathVariable Long jobId, Authentication authentication)
    {
        String email = authentication.getName();
        JobDto jobDto =  userService.saveJob(email, jobId);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobDto);
    }
    @DeleteMapping(value = "/saved-jobs/{jobId}/jobseeker", version = "1.0")
    public ResponseEntity<String> unsaveJob(@PathVariable Long jobId, Authentication authentication)
    {
        String email = authentication.getName();
        userService.unsaveJob(email, jobId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Job unsaved successfully");
    }
    @PostMapping(value = "/job-applications/jobseeker", version = "1.0")
    public ResponseEntity<JobApplicationDto> applyJob(@RequestBody @Valid ApplyJobRequestDto applyJobRequestDto,
                                                      Authentication authentication)
    {
        String email = authentication.getName();
        JobApplicationDto appliedJob = userService.applyForJob(email, applyJobRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(appliedJob);
    }

    @DeleteMapping(value = "/job-applications/{jobId}/jobseeker", version = "1.0")
    public ResponseEntity<String> withdrawJob(@PathVariable Long  jobId,
                                                      Authentication authentication)
    {
        String email = authentication.getName();
        userService.withdrawApplication(email, jobId);
        return ResponseEntity.status(HttpStatus.OK).body("Application withdrawed successfully");
    }

    @GetMapping(value = "/job-applications/jobseeker", version = "1.0")
    public ResponseEntity<List<JobApplicationDto>> getJobApplications(Authentication authentication)
    {
        String email = authentication.getName();
        List<JobApplicationDto> jobApplicationDtos = userService.getJobSeekerApplications(email);
        return ResponseEntity.status(HttpStatus.OK).body(jobApplicationDtos);
    }
}
