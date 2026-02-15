package com.tourism.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.tourism.entity.Place;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class PlaceDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Place> findByCityId(Integer cityId) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery(
            "FROM Place p WHERE p.city.cityId = :cityId",
            Place.class
        ).setParameter("cityId", cityId)
         .getResultList();
    }
}
