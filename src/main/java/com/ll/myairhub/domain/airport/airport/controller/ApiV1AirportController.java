package com.ll.myairhub.domain.airport.airport.controller;


import com.ll.myairhub.domain.airport.airport.service.AirportService;
import com.ll.myairhub.global.msg.Msg;
import com.ll.myairhub.global.rsData.RsData;
import com.ll.myairhub.standard.base.Empty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/airport")
@RequiredArgsConstructor
public class ApiV1AirportController {

    private final AirportService airportService;

    @PostMapping("/add")
    public RsData<Empty> getAirports() {

        airportService.updateAirportData();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg());
    }

}
