package com.tourism.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tourism.dao.CityDao;
import com.tourism.dao.PlaceDao;
import com.tourism.dao.StateDao;
import com.tourism.dto.CityDTO;
import com.tourism.dto.PlaceDTO;
import com.tourism.dto.StateDTO;

@Service
@Transactional
public class ExploreService {

    private final StateDao stateDao;
    private final CityDao cityDao;
    private final PlaceDao placeDao;

    public ExploreService(StateDao stateDao, CityDao cityDao, PlaceDao placeDao) {
        this.stateDao = stateDao;
        this.cityDao = cityDao;
        this.placeDao = placeDao;
    }

    public List<StateDTO> getStates() {
        return stateDao.findAll()
                .stream()
                .map(s -> new StateDTO(s.getStateId(), s.getStateName()))
                .collect(Collectors.toList());
    }

    public List<CityDTO> getCities(Integer stateId) {
        return cityDao.findByStateId(stateId)
                .stream()
                .map(c -> new CityDTO(c.getCityId(), c.getCityName()))
                .collect(Collectors.toList());
    }

    public List<PlaceDTO> getPlaces(Integer cityId) {
        return placeDao.findByCityId(cityId)
                .stream()
                .map(p -> new PlaceDTO(
                        p.getPlaceId(),
                        p.getPlaceName(),
                        p.getIsAsiMonument(),
                        p.getEntryFee()
                ))
                .collect(Collectors.toList());
    }
}
