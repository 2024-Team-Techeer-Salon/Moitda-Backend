package com.techeersalon.moitda.domain.meetings.dto.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointMapper {
    private double latitude;
    private double longitude;

    public static PointMapper from(double latitude, double longitude){
        return PointMapper.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
