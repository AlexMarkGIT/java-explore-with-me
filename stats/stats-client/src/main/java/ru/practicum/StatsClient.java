package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public StatsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void post(HitDto hitDto) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        restTemplate.exchange("http://localhost:9090/hit",
                HttpMethod.POST,
                new HttpEntity<>(hitDto, httpHeaders),
                HitDto.class);
    }

    @SneakyThrows
    public List<StatsDto> get(String start, String end, List<String> uris, Boolean unique) {

        Map<String, Object> requestParameters = new HashMap<>();
        requestParameters.put("start", start);
        requestParameters.put("end", end);
        requestParameters.put("uris", uris);
        requestParameters.put("unique", unique);

        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity("http://localhost:9090/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                        String.class,
                        requestParameters);

        return Arrays.asList(objectMapper.readValue(responseEntity.getBody(), StatsDto[].class));
    }



}
