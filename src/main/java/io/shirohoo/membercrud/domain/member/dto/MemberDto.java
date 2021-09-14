package io.shirohoo.membercrud.domain.member.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberDto {
    @NotNull(message = "please enter your id.")
    private String username;

    @NotNull(message = "please enter your password.")
    private String password;

    private MemberDto() {
    }

    private MemberDto(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public static MemberDto of(final String username, final String password) {
        return new MemberDto(username, password);
    }
}
