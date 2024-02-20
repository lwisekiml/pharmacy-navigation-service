package com.example.project.direction.entity;

import com.example.project.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "direction")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Direction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 고객
    String inputAddress;
    double inputLatitude;
    double inputLongitude;

    // 약국
    String targetPharmacyName;
    String targetAddress;
    double targetLatitude;
    double targetLongitude;

    // 고객 주소 와 약국 주소 사이의 거리
    double distance;
}
