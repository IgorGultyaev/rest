package org.example.rest.security;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.time.Instant;
import java.util.Set;

@RequiredArgsConstructor
public class AppAuthentication implements Principal {
    private final AppUserDetails details;

    @Override
    public String getName() {
        return details.getLogin();
    }

    public boolean isAnonymous() {
        return details.getRoles().contains("ROLE_ANONYMOUS");
    }

    public AppUserDetails getPrincipal() {
        return this.details;
    }

    public static class AnonymousAuthentication extends AppAuthentication {
        public AnonymousAuthentication() {
            super(new AppUserDetails(
                    -1,
                    "anonymous",
                    "***",
                    new AppUserDetails.Profile("anonymous.svg"),
                    Set.of("ROLE_ANONYMOUS"),
                    true,
                    true,
                    true,
                    true,
                    Instant.EPOCH
            ));
        }
    }
}
