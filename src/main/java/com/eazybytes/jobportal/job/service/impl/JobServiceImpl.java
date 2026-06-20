package com.eazybytes.jobportal.job.service.impl;

import com.eazybytes.jobportal.dto.JobApplicationDto;
import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.dto.UpdateJobApplicationDto;
import com.eazybytes.jobportal.entity.Job;
import com.eazybytes.jobportal.entity.JobApplication;
import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.job.service.IJobService;
import com.eazybytes.jobportal.repository.JobApplicationRepository;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.repository.JobRepository;
import com.eazybytes.jobportal.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class JobServiceImpl implements IJobService {

    private final JobPortalUserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Override
    public List<JobDto> getEmployerJobs(String email) {
        JobPortalUser employer = userRepository.findJobPortalUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        if(employer.getCompany() == null)
            throw new RuntimeException("Employer doesnt have company assigned");
        List<Job> employerJobs = employer.getCompany().getJobs();
        return employerJobs.stream()
                .map(ApplicationUtility::transformJobToDto)
                .toList();

    }

    @Override
    public JobDto updateJobStatus(Long jobId, String status, String email) {
        // Validate status
        if (!status.equals("ACTIVE") && !status.equals("CLOSED") && !status.equals("DRAFT")) {
            throw new RuntimeException("Invalid status. Must be ACTIVE, CLOSED, or DRAFT");
        }
        JobPortalUser employer = userRepository.findJobPortalUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }
        Job job = employer.getCompany().getJobs().stream().filter(j -> j.getId().equals(jobId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        return ApplicationUtility.transformJobToDto(job);

    }

    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, String employerEmail) {
        // Validate employer and get their company
        JobPortalUser employer = userRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned. Please contact admin.");
        }
        Job job = transformDtoToEntity(jobDto);
        job.setPostedDate(Instant.now());
        job.setApplicationsCount(0);
        //once job is created it need to go to status Draft. IN jobportal schema we have it as active,
        // to override that we mentioned draft.
        job.setStatus("DRAFT");
        job.setCompany(employer.getCompany());
        Job savedJob = jobRepository.save(job);
        return ApplicationUtility.transformJobToDto(savedJob);
    }

    private Job transformDtoToEntity(JobDto jobDto) {
        Job job = new Job();
        BeanUtils.copyProperties(jobDto, job);
        return job;
    }

    @Override
    public List<JobApplicationDto> getApplicationsByJobForEmployer(Long jobId) {

        List<JobApplication> applications = jobApplicationRepository.findByJobIdOrderByAppliedAtAsc(jobId);
        return applications.stream()
                .map(jobApplication -> ApplicationUtility.mapToJobApplicationDto(jobApplication))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean updateJobApplication(UpdateJobApplicationDto dto) {
        int updatedRows = jobApplicationRepository.updateStatusAndNotesById(
                dto.status().name(), dto.notes(),dto.applicationId(), ApplicationUtility.getLoggedInUser());
        return updatedRows > 0;
    }




}
