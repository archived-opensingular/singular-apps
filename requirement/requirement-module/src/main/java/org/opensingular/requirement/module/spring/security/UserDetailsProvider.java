package org.opensingular.requirement.module.spring.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class UserDetailsProvider {

    public UserDetails get() {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    public <T extends UserDetails> T get(Class<T> expectedClass) {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            return expectedClass.cast(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        return null;
    }


    public <T extends UserDetails> Optional<T> getOptional(Class<T> expectedClass) {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            return Optional.of(expectedClass.cast(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        }
        return Optional.empty();
    }
}