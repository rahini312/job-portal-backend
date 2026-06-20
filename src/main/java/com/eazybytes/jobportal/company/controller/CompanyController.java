package com.eazybytes.jobportal.company.controller;

import com.eazybytes.jobportal.company.service.ICompanyService;
import com.eazybytes.jobportal.dto.CompanyDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final ICompanyService companyService;

    // @LogAspect
    @GetMapping(path = "/public", version = "1.0")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<CompanyDto> companyList = companyService.getAllCompanies();
        //throw new RuntimeException("Exception occurred");
        return ResponseEntity.ok().body(companyList);
    }

    @PostMapping(path = "/admin", version = "1.0")
    public ResponseEntity<String> createCompany(@RequestBody @Valid CompanyDto companyDto) {
        boolean isCreated = companyService.createCompany(companyDto);
        if(isCreated) {
            return ResponseEntity.status(HttpStatus.OK).body("Company created successfully");
        }
        else  {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create company");
        }
    }

    @GetMapping(path = "/admin", version = "1.0")
    public ResponseEntity<List<CompanyDto>> getAllCompaniesForAdmin(){
        List<CompanyDto> companyList = companyService.getAllCompaniesForAdmin();
        return ResponseEntity.status(HttpStatus.OK).body(companyList);
    }

    @PutMapping(path = "/{id}/admin", version = "1.0")
    public ResponseEntity<String> updateCompanyDetails(@PathVariable @NotBlank String id,
                                                       @RequestBody @Valid CompanyDto companyDto) {
        boolean isCreated = companyService.updateCompanyDetails(Long.valueOf(id), companyDto);
        if(isCreated) {
            return ResponseEntity.status(HttpStatus.OK).body("Company updated successfully");
        }
        else  {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update company details");
        }
    }


    @DeleteMapping(path = "/{id}/admin", version = "1.0")
    public ResponseEntity<String> deleteCompanyById(@PathVariable @NotBlank String id) {
        companyService.deleteCompanyById(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Company record deleted successfully.");
    }

}
