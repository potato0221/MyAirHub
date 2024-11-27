package com.ll.myairhub.domain.airport.airport.entity;

import com.ll.myairhub.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class Airport extends BaseEntity {

    private String region;

    private String city;

    private String country;

    private String iataCode;

    private String icaoCode;

    private String airportNameKr;

    private String airportNameEng;

    //위도
    private double latitude;

    //경도
    private double longitude;
}
