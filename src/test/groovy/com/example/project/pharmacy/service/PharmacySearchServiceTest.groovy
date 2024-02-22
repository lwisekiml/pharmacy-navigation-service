package com.example.project.pharmacy.service

import com.example.project.pharmacy.cache.PharmacyRedisTemplateService
import com.example.project.pharmacy.entity.Pharmacy
import com.google.common.collect.Lists
import spock.lang.Specification

class PharmacySearchServiceTest extends Specification {

    private PharmacySearchService pharmacySearchService

    private PharmacyRepositoryService pharmacyRepositoryService = Mock()
    private PharmacyRedisTemplateService pharmacyRedisTemplateService = Mock()

    private List<Pharmacy> pharmacyList

    def setup() {
        pharmacySearchService = new PharmacySearchService(pharmacyRepositoryService, pharmacyRedisTemplateService)

        pharmacyList = Lists.newArrayList(
                Pharmacy.builder()
                        .id(1L)
                        .pharmacyName("호수온누리약국")
                        .latitude(37.60894036)
                        .longitude(127.029052)
                        .build(),
                Pharmacy.builder()
                        .id(2L)
                        .pharmacyName("돌곶이온누리약국")
                        .latitude(37.61040424)
                        .longitude(127.0569046)
                        .build())
    }

    def "레디스 장애시 DB를 이용하여 약국 데이터 조회"() {
        when:
        pharmacyRedisTemplateService.findAll() >> [] // findAll()을 호출했을 때 빈 리스트 호출
        pharmacyRepositoryService.findAll() >> pharmacyList

        // searchPharmacyDtoList() 호출시 안에 pharmacyRedisTemplateService.findAll()이 호출되고 빈 리스트를 준다.
        // 그래서 DB의 pharmacyRepositoryService.findAll() 을 호출한다.
        def result = pharmacySearchService.searchPharmacyDtoList()

        then:
        result.size() == 2
    }
}
