package com.eazybytes.jobportal.company.service;

import com.eazybytes.jobportal.dto.CompanyDto;

import java.util.List;

public interface ICompanyService {

    List<CompanyDto> getAllCompanies();

    List<CompanyDto> getAllCompaniesForAdmin();
    boolean createCompany(CompanyDto companyDto);
    boolean updateCompanyDetails(Long id, CompanyDto companyDto);

    void deleteCompanyById(Long id);
}
