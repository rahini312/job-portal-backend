package com.eazybytes.jobportal.audit;

import com.eazybytes.jobportal.util.ApplicationUtility;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        //Currently whenever any update is made, it will be displayed as anonymous in DB.
        // To overcome that get username from response and store that

        //return Optional.of("Anonymous User");
        return Optional.of(ApplicationUtility.getLoggedInUser());
    }
}
