package org.example.rest.filter;


import lombok.RequiredArgsConstructor;
import org.example.rest.exception.AppException;
import org.example.rest.exception.AppForbiddenException;
import org.example.rest.security.AppAuthentication;
import org.example.rest.security.AppUserDetails;
import org.example.rest.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;


//@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final UserService service;
    private final PasswordEncoder encoder;
    private final String dummyPassword = "hash dummy-password";


    // Basic:
    //   Authorization: Basic base64(username:password)
    // Custom:
    //   X-Login: login
    //   X-Password: password
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String login = request.getHeader("X-Login");
        String password = request.getHeader("X-Password");


        if (login == null && password == null) {
            HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
                @Override
                public Principal getUserPrincipal() {
                    return new AppAuthentication.AnonymousAuthentication();
                }
            };
            filterChain.doFilter(wrappedRequest, response);
            return;
        }


        // FIXME: timing attack
        try {
            AppUserDetails appUserDetails = this.service.loadUserByUsername(login);


            if (!encoder.matches(password, appUserDetails.getPassword())) {
                throw new AppForbiddenException();
            }
            // FIXME: clear private credentials
            appUserDetails.setPassword("***");


            HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
                @Override
                public Principal getUserPrincipal() {
                    return new AppAuthentication(appUserDetails);
                }
            };
            filterChain.doFilter(wrappedRequest, response);
        } catch (AppException e) {
            boolean matchResult = encoder.matches(password, dummyPassword);
            // TODO: consume matchResult
            throw new AppForbiddenException();
        }
    }
}


