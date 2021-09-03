package io.shirohoo.membercrud.config.security.provider;

import io.shirohoo.membercrud.config.security.model.MemberAuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public final class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final MemberAuthenticationContext context = (MemberAuthenticationContext) userDetailsService.loadUserByUsername(authentication.getName());
        final String encryptedPassword = context.getPassword();
        final String enteredPassword = (String) authentication.getCredentials();

        if (!passwordEncoder.matches(enteredPassword, encryptedPassword)) {
            throw new BadCredentialsException("password not matched !");
        }

        return new UsernamePasswordAuthenticationToken(context.getUsername(), null, context.getAuthorities());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return isUsernamePasswordAuthenticationToken(authentication);
    }

    private boolean isUsernamePasswordAuthenticationToken(final Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
