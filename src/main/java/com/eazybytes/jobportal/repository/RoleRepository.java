package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    //Reading roles from the DB each time, will cost more and degrades performance
    //When used cacheable, the role will be copied to cache which reduces DB reads and increases performance
    //Cacheable uses concurrent hashmap to store key value pairs. Since here name is passes as input parameter, it will
    //be stored as name, roles.
    @Cacheable("roles")
    Optional<Role> findRoleByName(String name);

}