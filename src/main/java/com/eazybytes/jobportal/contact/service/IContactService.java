package com.eazybytes.jobportal.contact.service;

import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IContactService {

    boolean saveContact(ContactRequestDto contactRequestDto);
    boolean closeContactMsg(Long id,  String status_);
    List<ContactResponseDto> fetchNewContactMsgs();
    List<ContactResponseDto> fetchNewContactMsgsWithSort(String sortBy, String sortDir);
    //Objects returned will be wrapped using page
    Page<ContactResponseDto> fetchNewContactMsgsWithPaginationSort(int pageNumber, int pageSize, String sortBy, String sortDir);

}
