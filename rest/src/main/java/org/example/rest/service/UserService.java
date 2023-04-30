package org.example.rest.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.rest.exception.AppException;
import org.example.rest.security.AppUserDetails;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final NamedParameterJdbcOperations operations;
    private final ObjectMapper objectMapper;


    public AppUserDetails loadUserByUsername(String username) {
        return this.operations.query(
                        "SELECT \"id\", \"login\", \"password\", \"profile\", \"roles\", \"account_non_expired\", \"account_non_locked\", \"credentials_non_expired\", \"enabled\", \"created\" FROM \"users\" WHERE \"login\" = :username LIMIT 1",
                        Map.of("username", username),
                        (rs, rowNum) -> {
                            try {
                                return new AppUserDetails(
                                        rs.getLong("id"),
                                        rs.getString("login"),
                                        rs.getString("password"),
                                        objectMapper.readValue(rs.getString("profile"), AppUserDetails.Profile.class),
                                        Set.of((String[]) rs.getArray("roles").getArray()),
                                        rs.getBoolean("account_non_expired"),
                                        rs.getBoolean("account_non_locked"),
                                        rs.getBoolean("credentials_non_expired"),
                                        rs.getBoolean("enabled"),
                                        rs.getTimestamp("created").toInstant()


                                );
                            } catch (JsonProcessingException e) {
                                throw new AppException(e);
                            }
                        }
                ).stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}


