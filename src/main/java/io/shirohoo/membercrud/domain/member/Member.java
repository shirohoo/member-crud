package io.shirohoo.membercrud.domain.member;

import io.shirohoo.membercrud.config.security.type.Role;
import io.shirohoo.membercrud.domain.AbstractEntity;
import io.shirohoo.membercrud.domain.member.dto.MemberDto;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Member(final String username, final String password) {
        this.username = username;
        this.password = password;
        this.role = Role.USER;
    }

    public static Member of(final String username, final String password) {
        return new Member(username, password);
    }

    public static Member of(final MemberDto memberDto) {
        return new Member(memberDto.getUsername(), memberDto.getPassword());
    }

    public UserDetails toUserDetails() {
        return User.builder()
                .username(username)
                .password(password)
                .authorities(getGrantedAuthorities())
                .build();
    }

    public Member encryptPassword(final PasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
        return this;
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getRole()));
    }

    public Member updatePassword(final MemberDto memberDto) {
        this.password = memberDto.getPassword();
        return this;
    }

}
