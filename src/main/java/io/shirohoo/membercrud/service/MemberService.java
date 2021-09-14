package io.shirohoo.membercrud.service;

import io.shirohoo.membercrud.domain.member.Member;
import io.shirohoo.membercrud.domain.member.MemberRepository;
import io.shirohoo.membercrud.domain.member.dto.MemberDto;
import io.shirohoo.membercrud.exception.AlreadyExistsUsername;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder encoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(final MemberDto request) throws AlreadyExistsUsername {
        log.info("request to membership: {}", request.getUsername());
        verifyUsername(request);
        signUpComplete(request);
        log.info("membership successful: {}", request.getUsername());
    }

    private void verifyUsername(final MemberDto request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            log.warn("cannot sign-up because the id is already in use: {}", request.getUsername());
            throw new AlreadyExistsUsername();
        }
    }

    private void signUpComplete(final MemberDto request) {
        memberRepository.save(Member.of(request)
                .encryptPassword(encoder));
    }

    @Transactional
    public void update(final MemberDto request) {
        log.info("request to password update: {}", request.getUsername());
        updatePassword(findMemberByUsername(request), request);
        log.info("password update successful: {}", request.getUsername());
    }

    private void updatePassword(final Member member, final MemberDto request) {
        member.updatePassword(request)
                .encryptPassword(encoder);
    }

    private Member findMemberByUsername(final MemberDto request) {
        return memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("user not found !"));
    }

    @Transactional
    public void delete(final MemberDto request) {
        log.info("request to cancel membership: {}", request.getUsername());
        memberRepository.delete(findMemberByUsername(request));
        log.info("cancel membership successful: {}", request.getUsername());
    }
}
