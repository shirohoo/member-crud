package io.shirohoo.membercrud.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.shirohoo.membercrud.web.model.HttpResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class UsernamePasswordAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public UsernamePasswordAuthenticationFailureHandler(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException ex) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String errMsg = "username or password is invalid.";

        if (ex instanceof BadCredentialsException) {
            errMsg = ex.getMessage();
        } else if (ex instanceof UsernameNotFoundException) {
            errMsg = ex.getMessage();
        } else if (ex instanceof InternalAuthenticationServiceException) {
            errMsg = ex.getMessage();
        }

        objectMapper.writeValue(response.getWriter(),
            HttpResponse.of(HttpStatus.UNAUTHORIZED, errMsg));
    }
}
