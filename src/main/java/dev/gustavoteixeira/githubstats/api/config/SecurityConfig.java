package dev.gustavoteixeira.githubstats.api.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/statistics").permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-ui.html/*/*").permitAll()
                .antMatchers(HttpMethod.GET, "/v2/api-docs",
                                             "/configuration/ui",
                                             "/swagger-resources/**",
                                             "/configuration/security",
                                             "/swagger-ui.html",
                                             "/webjars/**").permitAll()
                .antMatchers( "/actuator/health").permitAll()
                .anyRequest().denyAll();
    }

}