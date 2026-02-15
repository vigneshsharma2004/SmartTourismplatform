package com.tourism.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.tourism.entity.City;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class CityDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<City> findByStateId(Integer stateId) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(
            "FROM City c WHERE c.state.stateId = :stateId",
            City.class
        ).setParameter("stateId", stateId)
         .getResultList();
    }
}


