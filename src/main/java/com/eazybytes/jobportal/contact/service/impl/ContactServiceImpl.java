package com.eazybytes.jobportal.contact.service.impl;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import com.eazybytes.jobportal.entity.Contact;
import com.eazybytes.jobportal.repository.ContactRepository;
import com.eazybytes.jobportal.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @Override
    @Transactional
    public boolean saveContact(ContactRequestDto contactRequestDto) {
        boolean result = false;
        Contact contact = contactRepository.save(transformToEntity(contactRequestDto));
        if(contact != null && contact.getId() != null) {
            result = true;
        }
        return result;
    }

    private Contact transformToEntity(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto, contact);
//        contact.setCreatedAt(Instant.now());
//        contact.setCreatedBy("System");
        contact.setStatus(ApplicationConstants.NEW_MESSAGE);
        return contact;
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgs() {
        List<Contact> contacts = contactRepository.findContactsByStatusOrderByCreatedAtDesc(ApplicationConstants.NEW_MESSAGE);
        List<ContactResponseDto> responseDtos = contacts.stream()
                .map(this::transformToDto)
                .collect(Collectors.toList());
        return responseDtos;
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgsWithSort(String sortBy, String sortDir) {
        //Based on what params sent in API URL, that sorting will happen here. dynamic sorting
        Sort sort = sortBy.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        List<Contact> contacts = contactRepository.findContactsByStatus(ApplicationConstants.NEW_MESSAGE, sort);
        List<ContactResponseDto> responseDtos = contacts.stream()
                .map(this::transformToDto)
                .collect(Collectors.toList());
        return responseDtos;
    }

    @Override
    public Page<ContactResponseDto> fetchNewContactMsgsWithPaginationSort(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortBy.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        //Creating Pageable object with page number, size and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        //Fetch paginated and sorted contacts from repo
        Page<Contact> contactPage = contactRepository.findContactsByStatus(ApplicationConstants.NEW_MESSAGE, pageable);
        Page<ContactResponseDto> responseDtoPage = contactPage.map(this::transformToDto);

        return responseDtoPage;
    }

    @Override
    //This method is not readOnly, hence we need to mention as transactional.If we mention transactional for all read,
    //write methods, then performance will be impacted.
    //Transactional is mentioned so that whenever any null pointer exception occurs, rollback should happen,
    // All changes done in this method need to be reverted. But this is applicable only for runtime exceptions.
    //For Checked exceptions, it is NA, To make applicable rollbackFor = Exception.class so that rollback occurs for all exceptions
   // @Transactional(rollbackFor = Exception.class)
    //@Transactional(timeout = 10) waits for 10 secs if no response then rollsback
    @Transactional
    public boolean closeContactMsg(Long id, String status) {
        /*Contact contact = contactRepository.findById(id).orElse(null); //SELECT
        if (contact == null) {
            return false;
        }
        contact.setStatus(status);
        contactRepository.save(contact); //UPDATE
        return true;*/
        //Above method involves 2 DB operations hence to optimize, below method is used

        int updatedRows = contactRepository.updateStatusById(status, id, ApplicationUtility.getLoggedInUser());
        //throw new NullPointerException();
        return updatedRows > 0;
    }

    private ContactResponseDto transformToDto(Contact contact) {
        ContactResponseDto contactResponseDto = new ContactResponseDto(contact.getId(),
                contact.getName(), contact.getEmail(), contact.getUserType(), contact.getSubject(),
                contact.getMessage(), contact.getStatus(), contact.getCreatedAt());
        return contactResponseDto;
    }

}
