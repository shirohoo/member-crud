package io.shirohoo.membercrud.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.shirohoo.membercrud.config.security.filter.UsernamePasswordLoginProcessingFilter;
import io.shirohoo.membercrud.config.security.handler.UsernamePasswordAuthenticationFailureHandler;
import io.shirohoo.membercrud.config.security.handler.UsernamePasswordAuthenticationSuccessHandler;
import io.shirohoo.membercrud.config.security.provider.UsernamePasswordAuthenticationProvider;
import io.shirohoo.membercrud.config.security.service.UsernamePasswordService;
import io.shirohoo.membercrud.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring()
            .antMatchers("/h2-console/**")
            .requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
            );
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().disable() // 편의상 비활성화

            .authorizeRequests(
                authorize -> authorize
                    .antMatchers(HttpMethod.GET, "/").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/member").permitAll()
                    .anyRequest().authenticated()
            )

            .formLogin().disable()

            .logout(logout -> logout
                .logoutUrl("/api/v1/logout")
                .logoutSuccessUrl("/")
            )

            .addFilterAt(usernamePasswordLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationProvider usernamePasswordAuthenticationProvider() {
        return new UsernamePasswordAuthenticationProvider(passwordEncoder(), userDetailsService());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UsernamePasswordService(memberRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UsernamePasswordLoginProcessingFilter usernamePasswordLoginProcessingFilter() throws Exception {
        UsernamePasswordLoginProcessingFilter filter = new UsernamePasswordLoginProcessingFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(usernamePasswordAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(usernamePasswordAuthenticationFailureHandler());
        return filter;
    }

    @Bean
    public AuthenticationSuccessHandler usernamePasswordAuthenticationSuccessHandler() {
        return new UsernamePasswordAuthenticationSuccessHandler(objectMapper);
    }

    @Bean
    public AuthenticationFailureHandler usernamePasswordAuthenticationFailureHandler() {
        return new UsernamePasswordAuthenticationFailureHandler(objectMapper);
    }
}
