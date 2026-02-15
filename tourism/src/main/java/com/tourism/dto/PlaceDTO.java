package com.tourism.dto;

import java.math.BigDecimal;

public class PlaceDTO {

    private Integer placeId;
    private String placeName;
    private Boolean isAsiMonument;
    private BigDecimal entryFee;

    public PlaceDTO() {
    }

    public PlaceDTO(Integer placeId, String placeName,
                    Boolean isAsiMonument, BigDecimal entryFee) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.isAsiMonument = isAsiMonument;
        this.entryFee = entryFee;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setIsAsiMonument(Boolean isAsiMonument) {
        this.isAsiMonument = isAsiMonument;
    }

    public void setEntryFee(BigDecimal entryFee) {
        this.entryFee = entryFee;
    }

    public Integer getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Boolean getIsAsiMonument() {
        return isAsiMonument;
    }

    public BigDecimal getEntryFee() {
        return entryFee;
    }
}
