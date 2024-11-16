package com.ll.myairhub.domain.member.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class NicknameDto {

    private String nickname;

    public NicknameDto(String nickname) {
        this.nickname = nickname;
    }
}
