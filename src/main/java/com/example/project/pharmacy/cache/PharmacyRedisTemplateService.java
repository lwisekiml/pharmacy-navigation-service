package com.example.project.pharmacy.cache;

import com.example.project.pharmacy.dto.PharmacyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRedisTemplateService {

    private static final String CACHE_KEY = "PHARMACY";

    private final RedisTemplate<String, Object> redisTemplate;

    /* 빈으로 등록 해놓고 싱글톤으로 사용하려고 하고 있고 config에 등록을 해주어 사용(ObjectMapperConfig)
     * 약국 데이터를 조회하거나 저장할 때 serialize, deserialize를 ObjectMapper를 이용해서 사용할 건데 그 때 마다 인스턴스를 생성하기 보다
     * 하나의 싱글톤으로 구성해두고, 이 빈으로 사용할 것이다.
     */
    private final ObjectMapper objectMapper;

    //                  CACHE_KEY, subkey(약국 데이터의 PK값, DB 시퀀스에서 제공해주는 PK값), 약국 DTO를 스트링 형태로 변경
    // ObjectMapper를 이용해서 serialize를 해서 json 형태로 저장할 것이다.
    private HashOperations<String, String, String> hashOperations;

    /**
     * @PostConstruct 의존성 주입이 완료된 후에 실행되어야 하는 method에 사용!!!
     * 해당 어노테이션은 다른 리소스에서 호출되지 않아도 수행
     * 생성자 보다 늦게 호출된다.
     *
     * [호출 순서]
     * 1. 생성자 호출
     * 2. 의존성 주입 완료 (@Autowired || @RequiredArgsConstructor )
     * 3. @PostConstruct
     */
    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(PharmacyDto pharmacyDto) {
        if(Objects.isNull(pharmacyDto) || Objects.isNull(pharmacyDto.getId())) {
            log.error("Required Values must not be null");
            return;
        }

        try {
            hashOperations.put(CACHE_KEY,
                    pharmacyDto.getId().toString(),
                    serializePharmacyDto(pharmacyDto)); // PharmacyDto -> JSON
            log.info("[PharmacyRedisTemplateService save success] id: {}", pharmacyDto.getId());
        } catch (Exception e) {
            log.error("[PharmacyRedisTemplateService save error] {}", e.getMessage());
        }
    }


    public List<PharmacyDto> findAll() {

        try {
            List<PharmacyDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {
                PharmacyDto pharmacyDto = deserializePharmacyDto(value); // JSON -> PharmacyDto
                list.add(pharmacyDto);
            }
            return list;

        } catch (Exception e) {
            log.error("[PharmacyRedisTemplateService findAll error]: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 로직에서는 사용X, 테스트 메소드를 사용하기 위해 추가
    public void delete(Long id) {
        hashOperations.delete(CACHE_KEY, String.valueOf(id));
        log.info("[PharmacyRedisTemplateService delete]: {} ", id);
    }

    // PharmacyDto -> JSON
    private String serializePharmacyDto(PharmacyDto pharmacyDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(pharmacyDto);
    }

    // JSON -> PharmacyDto
    private PharmacyDto deserializePharmacyDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, PharmacyDto.class); // value 값을 PharmacyDto로 변환
    }
}
