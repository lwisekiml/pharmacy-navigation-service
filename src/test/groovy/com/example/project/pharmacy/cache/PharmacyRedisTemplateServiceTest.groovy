package com.example.project.pharmacy.cache

import com.example.project.AbstractIntegrationContainerBaseTest
import com.example.project.pharmacy.dto.PharmacyDto
import org.springframework.beans.factory.annotation.Autowired

class PharmacyRedisTemplateServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private PharmacyRedisTemplateService pharmacyRedisTemplateService;

    def setup() { // 각각 테스트마다 의존성이 생기지 않게 테스트 전에 redis를 모두 비워준다.
        pharmacyRedisTemplateService.findAll()
                .forEach(dto -> {
                    pharmacyRedisTemplateService.delete(dto.getId()) // key값을 제거하여 여러 테스트 메소드를 만들었을 때 테스트 메소드끼리 의존성 영향을 받지 않는다.
                })
    }

    def "save success"() {
        given:
        String pharmacyName = "name"
        String pharmacyAddress = "address"
        PharmacyDto dto =
                PharmacyDto.builder()
                        .id(1L)
                        .pharmacyName(pharmacyName)
                        .pharmacyAddress(pharmacyAddress)
                        .build()

        when:
        pharmacyRedisTemplateService.save(dto)
        List<PharmacyDto> result = pharmacyRedisTemplateService.findAll()

        then:
        result.size() == 1
        result.get(0).id == 1L
        result.get(0).pharmacyName == pharmacyName
        result.get(0).pharmacyAddress == pharmacyAddress
    }

    def "success fail"() {
        given:
        PharmacyDto dto =
                PharmacyDto.builder().
                        build()

        when:
        pharmacyRedisTemplateService.save(dto)
        List<PharmacyDto> result = pharmacyRedisTemplateService.findAll()

        then:
        result.size() == 0
    }

    def "delete"() {
        given:
        String pharmacyName = "name"
        String pharmacyAddress = "address"
        PharmacyDto dto =
                PharmacyDto.builder()
                        .id(1L)
                        .pharmacyName(pharmacyName)
                        .pharmacyAddress(pharmacyAddress)
                        .build()

        when:
        pharmacyRedisTemplateService.save(dto)
        pharmacyRedisTemplateService.delete(dto.getId())
        def result = pharmacyRedisTemplateService.findAll()

        then:
        result.size() == 0
    }
}
