package org.example.rest.security;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppUserDetails implements UserDetails, CredentialsContainer {
    private long id;
    private String login;
    private String password;
    private Profile profile;
    private Set<String> roles;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Instant created;


    @Override
    public String getUsername() {
        return this.login;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toUnmodifiableSet());
    }


    @Override
    public void eraseCredentials() {
        this.password = "***";
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Profile {
        private String avatar;
    }


    public static Anonymous anonymous() {
        return new Anonymous();
    }


    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Anonymous extends AppUserDetails {
        public Anonymous() {
            super(-1, "anonymous", "***", new Profile("anonymous.svg"), Set.of("ROLE_ANONYMOUS"), true, true, true, true, Instant.EPOCH);
        }
    }
}


