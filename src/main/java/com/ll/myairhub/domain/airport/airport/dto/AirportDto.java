package com.ll.myairhub.domain.airport.airport.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportDto {

    @JsonProperty("지역")
    private String region;

    @JsonProperty("영문도시명")
    private String city;

    @JsonProperty("영문국가명")
    private String country;

    @JsonProperty("공항코드2(ICAO)")
    private String icaoCode;

    @JsonProperty("공항코드1(IATA)")
    private String iataCode;

    @JsonProperty("한글공항")
    private String airportNameKr;

    @JsonProperty("영문공항명")
    private String airportNameEng;


}
