package com.ll.myairhub.domain.member.member.controller;

import com.ll.myairhub.domain.member.member.dto.MemberDto;
import com.ll.myairhub.domain.member.member.dto.NicknameDto;
import com.ll.myairhub.domain.member.member.entity.Member;
import com.ll.myairhub.domain.member.member.service.MemberService;
import com.ll.myairhub.global.msg.Msg;
import com.ll.myairhub.global.rq.Rq;
import com.ll.myairhub.global.rsData.RsData;
import com.ll.myairhub.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {

    private final Rq rq;
    private final MemberService memberService;

    public record MeResponseBody(@NonNull MemberDto item) {
    }

    public record LoginResponseBody(@NonNull MemberDto item) {
    }

    public record LoginRequestBody(@NotBlank String username, @NotBlank String password) {
    }

    @GetMapping("/me")
    @Operation(summary = "멤버 조회")
    public RsData<MeResponseBody> getMe() {
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new MeResponseBody(
                        new MemberDto(rq.getMember())
                )
        );
    }

    @PostMapping(value = "/login")
    @Operation(summary = "로그인")
    public RsData<LoginResponseBody> login(@Valid @RequestBody LoginRequestBody body) {
        RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
                body.username,
                body.password
        );

        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().refreshToken());
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().accessToken());

        return authAndMakeTokensRs.newDataOf(
                new LoginResponseBody(
                        new MemberDto(authAndMakeTokensRs.getData().member())
                )
        );
    }

    @PostMapping("/logout")
    public RsData<Empty> logout() {
        rq.setLogout();

        return RsData.of(Msg.E200_6_LOGOUT_SUCCEED.getCode(),
                Msg.E200_6_LOGOUT_SUCCEED.getMsg());
    }

    @PutMapping("/modifyNickName")
    @Operation(summary = "닉네임 변경")
    public RsData<NicknameDto> modifyNickName(@RequestBody NicknameDto nicknameDto){

        Member member = memberService.modifyNickname(nicknameDto);

        NicknameDto modifyNickNameDto = new NicknameDto(member.getNickname());

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),
                Msg.E200_2_MODIFY_SUCCEED.getMsg(),
                modifyNickNameDto);
    }
}
