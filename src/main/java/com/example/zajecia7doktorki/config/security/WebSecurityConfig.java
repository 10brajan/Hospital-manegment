package com.example.zajecia7doktorki.config.security;

import com.example.zajecia7doktorki.config.security.jwt.CustomAccessDeniedHandler;
import com.example.zajecia7doktorki.config.security.jwt.CustomAuthenticationEntryPoint;
import com.example.zajecia7doktorki.config.security.jwt.JwtAuthenticationFilter;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import com.example.zajecia7doktorki.service.CustomerUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    private final CustomAuthenticationEntryPoint entryPoint;

    private final CustomerRepository customerRepository;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService(customerRepository);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        try {
            httpSecurity
                    .csrf() //> do poczytania co to jest
                    .disable()
                    .authorizeRequests((authz) -> {
                                try {
                                    authz
    //                                        .antMatchers(HttpMethod.POST, "/api/v1/doctors/**").hasAnyAuthority("ADMIN", "USER")
    //                                        .antMatchers(HttpMethod.GET, "./.sdassd").hasAnyAuthority("USER")
    //                                        .antMatchers(HttpMethod.GET, "/api/v1/doctors").hasAnyRole("USER")
    //                                        .antMatchers(HttpMethod.GET, DOCTORS_URL).permitAll()
    //                                        .antMatchers(HttpMethod.GET, "/api/v1/doctors/**/patients").permitAll()
    //                                        .antMatchers(HttpMethod.POST, "/api/v1/doctors").permitAll()
    //                                        .antMatchers(HttpMethod.DELETE, DOCTORS_URL).permitAll()
    //                                        .antMatchers(HttpMethod.DELETE, "/api/v1/doctors/**/appointments/cancel/**").permitAll()
    //                                        .antMatchers(HttpMethod.PUT, DOCTORS_URL).permitAll()
    //                                        .antMatchers(HttpMethod.GET, "/api/v1/patients").permitAll()
    //                                        .antMatchers(HttpMethod.GET, PATIENTS_URL).permitAll()
    //                                        .antMatchers(HttpMethod.GET, "/api/v1/patients/**/appointments").permitAll()
    //                                        .antMatchers(HttpMethod.POST, "/api/v1/patients").permitAll()
    //                                        .antMatchers(HttpMethod.PUT, "/api/v1/patients/**/makeAppointment/**").permitAll()
    //                                        .antMatchers(HttpMethod.DELETE, PATIENTS_URL).permitAll()
    //                                        .antMatchers(HttpMethod.PUT, PATIENTS_URL).permitAll()
    //                                        .antMatchers(HttpMethod.POST, "/api/v1/register/**").permitAll()
                                            .antMatchers(HttpMethod.POST, "/api/v1/register/**").permitAll()
                                            .antMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                                            .antMatchers("/api/v1/admins/***").hasAuthority("ADMIN")
    //                                        .antMatchers(HttpMethod.POST, "/api/v1/admins/***").hasAuthority("ADMIN")
    //                                        .antMatchers(HttpMethod.PUT, "/api/v1/admins/***").hasAuthority("ADMIN")
    //                                        .antMatchers(HttpMethod.DELETE, "/api/v1/admins/***").hasAuthority("ADMIN")
                                            .antMatchers("/api/v1/doctors/***").hasAnyAuthority("ADMIN", "USER")
    //                                        .antMatchers(HttpMethod.POST, "/api/v1/doctors/***").hasAnyAuthority("ADMIN", "USER")
    //                                        .antMatchers(HttpMethod.PUT, "/api/v1/doctors/***").hasAnyAuthority("ADMIN", "USER")
    //                                        .antMatchers(HttpMethod.DELETE, "/api/v1/doctors/***").hasAnyAuthority("ADMIN", "USER")
                                            .antMatchers( "/api/v1/patients/***").hasAnyAuthority("ADMIN", "USER")
    //                                        .antMatchers(HttpMethod.POST, "/api/v1/patients/***").hasAnyAuthority("ADMIN", "USER")
    //                                        .antMatchers(HttpMethod.PUT, "/api/v1/patients/***").hasAnyAuthority("ADMIN", "USER")
    //                                        .antMatchers(HttpMethod.DELETE, "/api/v1/patients/***").hasAnyAuthority("ADMIN", "USER")
                                            .anyRequest()
                                            .authenticated()
                                            .and()
                                            .sessionManagement()
                                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //-> o rodzajach SessionCreationPolicy i jak sie je wykorzystuje
                                            .and()
                                            .authenticationProvider(authenticationProvider()) // -> poczytaj o authenticationProvider
                                            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                            .exceptionHandling()
                                            .authenticationEntryPoint(entryPoint)
                                            .accessDeniedHandler(accessDeniedHandler());
                                } catch (Exception e) {
                                    log.error("Error occurred with filterChain {}", e.getMessage());
                                    throw new RuntimeException(e);
                                }
                            }
                    ).httpBasic(Customizer.withDefaults());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
