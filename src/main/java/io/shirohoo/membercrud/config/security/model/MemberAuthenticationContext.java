package io.shirohoo.membercrud.config.security.model;

import io.shirohoo.membercrud.domain.member.Member;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

@Getter
public class MemberAuthenticationContext extends User {

    private MemberAuthenticationContext(final Member member) {
        super(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
    }

    public static MemberAuthenticationContext of(final Member member) {
        return new MemberAuthenticationContext(member);
    }
}
