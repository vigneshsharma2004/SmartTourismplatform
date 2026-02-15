package com.tourism.dto;

public class CityDTO {

    private Integer cityId;
    private String cityName;

    public CityDTO(Integer cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }
}
