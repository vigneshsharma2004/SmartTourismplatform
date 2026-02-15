package com.tourism.dto;

public class StateDTO {

    private Integer stateId;
    private String stateName;

    public StateDTO(Integer stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public String getStateName() {
        return stateName;
    }
}
