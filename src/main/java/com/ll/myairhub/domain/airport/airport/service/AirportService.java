package com.ll.myairhub.domain.airport.airport.service;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.myairhub.domain.airport.airport.dto.AirportDto;
import com.ll.myairhub.domain.airport.airport.entity.Airport;
import com.ll.myairhub.domain.airport.airport.repository.AirportRepository;
import com.ll.myairhub.global.app.AppConfig;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirportService {

    private final AirportRepository airportRepository;

    public void updateAirportData() {

        if (airportRepository.count() > 0) {
            System.out.println("이미 데이터가 존재합니다. 요청을 취소 합니다.");
            return;
        }

        try {
            for(int i=1; i<=6; i++) {
                // 1. URL 설정
                StringBuilder urlBuilder = new StringBuilder("https://api.odcloud.kr/api/3051587/v1/uddi:007305db-cbc2-4554-8988-f9109b2dad10");
                // 2. 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
                urlBuilder.append("?" + URLEncoder.encode("page","UTF-8") + "=" + URLEncoder.encode(String.valueOf(i), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("perPage","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=" + AppConfig.openApiKey);
                // 3. URL 객체 생성.
                URL url = new URL(urlBuilder.toString());
                // 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 5. 통신을 위한 메소드 SET.
                conn.setRequestMethod("GET");
                // 6. 통신을 위한 Content-type SET.
                conn.setRequestProperty("Content-type", "application/json");
                // 7. 통신 응답 코드 확인.
                System.out.print(i + "번째 통신 결과 : ");
                if(conn.getResponseCode() == 200) {
                    System.out.println("성공");
                } else {
                    System.out.println("실패");
                }
                // 8. 전달받은 데이터를 BufferedReader 객체로 저장.
                BufferedReader br;
                if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                // 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                // 10. 객체 해제.
                br.close();
                conn.disconnect();

                ObjectMapper objectMapper = new ObjectMapper();

                // String -> JSON 변환
                JsonNode node1 = objectMapper.readTree(sb.toString());
                // JSON 안의 'data' 객체 추출
                JsonNode node2 = node1.findValue("data");

                if (node2 == null) {
                    System.out.println("데이터가 존재하지 않습니다.");
                    return; // 또는 빈 리스트로 처리
                }

                // JSON엔 존재하나 DTO에는 존재하지 않는 매핑 값에 대해 처리
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // DTO List 생성
                List<AirportDto> airportDtos = Arrays.asList(objectMapper.treeToValue(node2, AirportDto[].class));

                saveAirports(airportDtos);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void saveAirports(List<AirportDto> airportDtos) {

        for(AirportDto airportDto : airportDtos){

            boolean isNational = airportDto.getAirportNameEng().toLowerCase().contains("international");

            if(isNational){
                Airport airport = Airport.builder()
                        .region(airportDto.getRegion())
                        .city(airportDto.getCity())
                        .country(airportDto.getCountry())
                        .iataCode(airportDto.getIataCode())
                        .icaoCode(airportDto.getIcaoCode())
                        .airportNameKr(airportDto.getAirportNameKr())
                        .airportNameEng(airportDto.getAirportNameEng())
                        .build();

                airportRepository.save(airport);
            }
        }
    }

    @Transactional
    public void updateLatLongFromCSVAndCleanUp() throws IOException, CsvException {
        String csvFilePath = AppConfig.getCsvFilePath();
        // CSV 파일을 읽음
        CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
        // 헤더 부분을 스킵
        csvReader.skip(1);
        // 그 다음줄(실제 데이터 부터 읽음)
        List<String[]> rows = csvReader.readAll();

        // 새로운 CSV 파일을 쓸 FileWriter 준비 (저장 경로 설정)
        String outputCsvFilePath = AppConfig.getOutputCsvFilePath();  // 새로운 CSV 파일 경로
        FileWriter fileWriter = new FileWriter(outputCsvFilePath);
        CSVWriter csvWriter = new CSVWriter(fileWriter);

        // CSV 파일을 한 행씩 처리
        for (String[] row : rows) {
            double latitude = 0.0;
            double longitude = 0.0;

            // latitude와 longitude가 올바른 형식인지 체크
            try {
                latitude = Double.parseDouble(row[0]);
                longitude = Double.parseDouble(row[1]);
            } catch (NumberFormatException e) {
                // 잘못된 데이터는 건너뛰거나 기본값 처리
                System.err.println("Invalid latitude or longitude: " + row[0] + ", " + row[1]);
                continue;
            }

            String icaoCode = row[2];
            String iataCode = row[3];

            // icaoCode가 일치 하는 항공사 찾기
            Optional<Airport> airportOpt = airportRepository.findByIcaoCode(icaoCode);

            // 없다면 iataCode가 일치 하는 항공사 찾기
            if (!airportOpt.isPresent()) {
                airportOpt = airportRepository.findByIataCode(iataCode);
            }

            if (airportOpt.isPresent()) {
                // DB에 해당 항공사가 있으면, 위도와 경도를 업데이트하고 CSV에 다시 쓴다
                Airport airport = airportOpt.get();
                airport.setLatitude(latitude);
                airport.setLongitude(longitude);
                airportRepository.save(airport);  // 업데이트된 항공사 저장

                // 업데이트된 정보를 새로운 CSV 파일에 기록
                csvWriter.writeNext(row);
            }
        }

        // 파일을 다 쓴 후 리소스를 정리
        csvReader.close();
        csvWriter.close();
        fileWriter.close();
    }

}
