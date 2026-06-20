package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findContactsByStatus(String status);

    List<Contact> findContactsByStatusOrderByCreatedAtDesc(String status);

    List<Contact> findContactsByStatus(String status, Sort sort);

    Page<Contact> findContactsByStatus(String status, Pageable pageable);

    //For update, delete queries need to mention Modify annotation - use only when using named queries. Because spring jpa automatically knows when modifying
    //flushAutomatically = true, whatever is the current state before modifying, will commit the changes
    //clearAutomatically = true, to load the updated data again/ to refresh.
    @Modifying(flushAutomatically = true)
    int updateStatusById(@Param("status") String status, @Param("id") Long id, @Param("updatedBy") String updatedBy);

}