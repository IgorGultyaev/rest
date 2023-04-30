package org.example.rest.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic();
        http.authorizeRequests().anyRequest().permitAll();
        return http.build();
    }


//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(WebSecurity web) {
//        return (web) -> web.ignoring().antMatchers("/resources/**");
//    }
}


