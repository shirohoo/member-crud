package io.shirohoo.membercrud.config.security.service;

import io.shirohoo.membercrud.config.security.model.MemberAuthenticationContext;
import io.shirohoo.membercrud.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsernamePasswordService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return MemberAuthenticationContext.of(
            memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found !"))
        );
    }
}
