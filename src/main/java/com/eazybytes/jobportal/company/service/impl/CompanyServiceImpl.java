package com.eazybytes.jobportal.company.service.impl;

import com.eazybytes.jobportal.company.service.ICompanyService;
import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.dto.CompanyDto;
import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.entity.Company;
import com.eazybytes.jobportal.repository.CompanyRepository;
import com.eazybytes.jobportal.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//All public methods in this class are going to derive this configuration
@Transactional(readOnly = true)

public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;


    @Override
    //Here no input param is passed, so companies will be stored as value against a null key
    @Cacheable("companies")
    public List<CompanyDto> getAllCompanies() {
        List<Company> companyList =companyRepository.fetchCompaniesWithJobsByStatus(ApplicationConstants.ACTIVE_STATUS);
        return companyList.stream().map(this::transformCompanyToDto).collect(Collectors.toList());
    }

    private CompanyDto transformCompanyToDto(Company company) {
        List<JobDto> jobDtos = company.getJobs().stream()
                .map(ApplicationUtility::transformJobToDto)
                .collect(Collectors.toList());
        return new CompanyDto(company.getId(), company.getName(), company.getLogo(),
                company.getIndustry(), company.getSize(), company.getRating(),
                company.getLocations(), company.getFounded(), company.getDescription(),
                company.getEmployees(), company.getWebsite(), company.getCreatedAt(),jobDtos);
    }

    @Transactional
    @Override
    public boolean createCompany(CompanyDto companyDto) {
        Company company = transformCompanyDtoToEntity(companyDto);
        Company savedCompany = companyRepository.save(company);
        return savedCompany.getId() != null && savedCompany.getId() > 0;
    }
    @Override
    public List<CompanyDto> getAllCompaniesForAdmin() {

        List<Company> companyList =companyRepository.findAll();
        return companyList.stream().map(this::transformCompanyToDtoForAdmin).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean updateCompanyDetails(Long id, CompanyDto companyDto) {
        int updatedRecords = companyRepository.updateCompanyDetails(
                id,companyDto.name(),companyDto.logo(),
                companyDto.industry(),companyDto.size(),companyDto.rating(),
                companyDto.locations(),companyDto.founded(),companyDto.description(),
                companyDto.employees(),companyDto.website()
        );
        return updatedRecords > 0;
    }

    @Transactional
    @Override
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }

    private CompanyDto transformCompanyToDtoForAdmin(Company company) {
        return new CompanyDto(company.getId(), company.getName(), company.getLogo(),
                company.getIndustry(), company.getSize(), company.getRating(),
                company.getLocations(), company.getFounded(), company.getDescription(),
                company.getEmployees(), company.getWebsite(), company.getCreatedAt(),null);
    }

    private Company transformCompanyDtoToEntity(CompanyDto companyDto) {
        Company company = new Company();
        BeanUtils.copyProperties(companyDto, company);
        return company;
    }




}
