package io.shirohoo.membercrud.config.security.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.shirohoo.membercrud.domain.member.dto.MemberDto;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

public class UsernamePasswordLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;

    public UsernamePasswordLoginProcessingFilter(final ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/v1/login", "POST"));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException, IOException {
        if (!isJsonType(request)) {
            throw new InternalAuthenticationServiceException("request is not of application/json type.");
        }

        final MemberDto memberDto = objectMapper.readValue(request.getReader(), MemberDto.class);

        if (isMissingUsernameOrPassword(memberDto)) {
            throw new InternalAuthenticationServiceException("no username or password entered.");
        }

        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(memberDto.getUsername(), memberDto.getPassword()));
    }

    private boolean isJsonType(final HttpServletRequest request) {
        return APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getHeader("Content-Type"));
    }

    private boolean isMissingUsernameOrPassword(final MemberDto memberDto) {
        return !StringUtils.hasText(memberDto.getUsername()) || !StringUtils.hasText(memberDto.getPassword());
    }
}
