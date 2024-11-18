package com.ll.myairhub.domain.airport.airport.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportDto {

    @JsonProperty("구분")
    private String region;

    @JsonProperty("공항코드")
    private String airportCode;

    @JsonProperty("공항명(영문)")
    private String airportName;


}
