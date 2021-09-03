package io.shirohoo.membercrud.web.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.shirohoo.membercrud.domain.member.Member;
import io.shirohoo.membercrud.domain.member.MemberRepository;
import io.shirohoo.membercrud.domain.member.dto.MemberDto;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberApiControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public MemberApiControllerTest(final MockMvc mockMvc, final ObjectMapper objectMapper, final MemberRepository memberRepository, final PasswordEncoder encoder) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
        this.encoder = encoder;
    }

    @Test
    @DisplayName("회원가입")
    void signUp() throws Exception {
        //given
        final MemberDto memberDto = MemberDto.of("shirohoo", "password");
        final String request = objectMapper.writeValueAsString(memberDto);

        // when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member")
            .content(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        perform
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청 시 이미 가입 된 아이디가 존재하는 경우")
    void signUp_exception() throws Exception {
        //given
        final MemberDto memberDto = MemberDto.of("shirohoo", "password");
        memberRepository.save(Member.of(memberDto).encryptPassword(encoder));
        final String request = objectMapper.writeValueAsString(memberDto);

        // when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member")
            .content(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        perform
            .andExpect(status().isConflict())
            .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("비밀번호 변경")
    void update() throws Exception {
        // given
        final MemberDto memberDto = MemberDto.of("shirohoo", "password");
        memberRepository.save(Member.of(memberDto).encryptPassword(encoder));
        final String request = objectMapper.writeValueAsString(MemberDto.of("shirohoo", "drowssap"));

        // when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/member")
            .content(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        perform
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("비밀번호 변경 요청 시 아이디가 존재하지 않는 경우(=잘못 된 요청인 경우)")
    void update_exception() throws Exception {
        // given
        final MemberDto memberDto = MemberDto.of("shirohoo", "password");
        memberRepository.save(Member.of(memberDto).encryptPassword(encoder));
        final String request = objectMapper.writeValueAsString(MemberDto.of("does not exist id", "drowssap"));

        // when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/member")
            .content(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        perform
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("회원탈퇴")
    void delete() throws Exception {
        // given
        final MemberDto memberDto = MemberDto.of("shirohoo", "password");
        memberRepository.save(Member.of(memberDto).encryptPassword(encoder));
        final String request = objectMapper.writeValueAsString(memberDto);

        // when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/member")
            .content(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        perform
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("회원탈퇴 변경 요청 시 아이디가 존재하지 않는 경우(=잘못 된 요청인 경우)")
    void delete_exception() throws Exception {
        // given
        final MemberDto memberDto = MemberDto.of("shirohoo", "password");
        memberRepository.save(Member.of(memberDto).encryptPassword(encoder));
        final String request = objectMapper.writeValueAsString(MemberDto.of("does not exist id", "drowssap"));

        // when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/member")
            .content(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        perform
            .andExpect(status().isNotFound())
            .andDo(print());
    }
}
