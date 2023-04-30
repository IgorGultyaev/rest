//package org.example.rest.configuration;
//
//import org.example.rest.security.AppAuthentication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.context.annotation.RequestScope;
//import org.springframework.web.context.request.WebRequest;
//
//@Configuration(proxyBeanMethods = false)
//public class AppConfiguration {
//    @Bean
//    @RequestScope
//    public AppAuthentication authentication(WebRequest request) {
//        return (AppAuthentication) request.getUserPrincipal();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // TODO: RTM (Read The Manual)
//        return new BCryptPasswordEncoder();
//    }
//}

package org.example.rest.configuration;


import org.example.rest.security.AppUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.annotation.RequestScope;


@Configuration(proxyBeanMethods = false)
public class AppConfiguration {
    @Bean
    @RequestScope
    public AppUserDetails userDetails() {
        // TODO: add null checks & stuff
        return ((AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // TODO: RTM (Read The Manual)
        return new BCryptPasswordEncoder();
    }
}



