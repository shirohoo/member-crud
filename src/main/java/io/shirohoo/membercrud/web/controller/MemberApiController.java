package io.shirohoo.membercrud.web.controller;

import io.shirohoo.membercrud.domain.member.dto.MemberDto;
import io.shirohoo.membercrud.service.MemberService;
import io.shirohoo.membercrud.web.model.HttpResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody final MemberDto request) {
        memberService.signUp(request);
        return ResponseEntity.ok(HttpResponse.of(HttpStatus.OK));
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody final MemberDto request) {
        memberService.update(request);
        return ResponseEntity.ok(HttpResponse.of(HttpStatus.OK));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@Valid @RequestBody final MemberDto request) {
        memberService.delete(request);
        return ResponseEntity.ok(HttpResponse.of(HttpStatus.OK));
    }
}
