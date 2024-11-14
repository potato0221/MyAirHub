package com.ll.myairhub.domain.member.member.dto;

import com.ll.myairhub.domain.member.member.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;


import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class MemberDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private String nickname;
    @NonNull
    private String username;
    @NonNull
    private String profileImgUrl;
    @NonNull
    private List<String> authorities;
    @NonNull
    private int flightCount;



    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.nickname = member.getNickname();
        this.username = member.getUsername();
        this.profileImgUrl = member.getProfileImgUrlOrDefault();
        this.authorities = member.getAuthoritiesAsStringList();
        this.flightCount = member.getFlightCount();
    }
}
