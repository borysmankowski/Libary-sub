package com.example.borys_mankowski_test_10.security;


import com.example.borys_mankowski_test_10.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig {

    private static final String API_URL_PATTERN = "/api/v*/user/**";
    private static final String BOOKS_URL_PATTERN = "/api/v*/books/**";
    private static final String SUBSCRIPTION_URL_PATTERN = "/api/v*/subscriptions/**";
    private static final String GET_ALL_APP_USERS = "/api/v1/all/**";

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http,
                                                      HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http.csrf(csrfConfigurer ->
                csrfConfigurer.ignoringRequestMatchers(mvcMatcherBuilder.pattern(API_URL_PATTERN),
                        mvcMatcherBuilder.pattern(BOOKS_URL_PATTERN),
                        mvcMatcherBuilder.pattern(SUBSCRIPTION_URL_PATTERN),
                        mvcMatcherBuilder.pattern(GET_ALL_APP_USERS),
                        PathRequest.toH2Console())
        );

        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(mvcMatcherBuilder.pattern(API_URL_PATTERN)).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern(BOOKS_URL_PATTERN)).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern(SUBSCRIPTION_URL_PATTERN)).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern(GET_ALL_APP_USERS)).permitAll()

                        .requestMatchers(PathRequest.toH2Console()).authenticated()
                        .anyRequest().authenticated()

        );

        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(List.of(user, admin));
    }
}

