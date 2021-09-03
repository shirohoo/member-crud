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
        log.info(String.format("request to membership: %s", request.getUsername()));
        verifyUsername(request);
        signUpComplete(request);
        log.info(String.format("membership successful: %s", request.getUsername()));
    }

    private void verifyUsername(final MemberDto request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            log.warn(String.format("cannot sign-up because the ID is already in use: %s", request.getUsername()));
            throw new AlreadyExistsUsername();
        }
    }

    private void signUpComplete(final MemberDto request) {
        memberRepository.save(Member.of(request)
            .encryptPassword(encoder));
    }

    @Transactional
    public void update(final MemberDto request) {
        log.info(String.format("request to password update: %s", request.getUsername()));
        updatePassword(findMemberByUsername(request), request);
        log.info(String.format("password update successful: %s", request.getUsername()));
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
        log.info(String.format("request to cancel membership: %s", request.getUsername()));
        deleteMember(findMemberByUsername(request));
        log.info(String.format("cancel membership successful: %s", request.getUsername()));
    }

    private void deleteMember(final Member member) {
        memberRepository.delete(member);
    }
}
