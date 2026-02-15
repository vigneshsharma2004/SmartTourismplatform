package com.tourism.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.tourism.entity.State;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<State> findAll() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM State", State.class)
                      .getResultList();
    }
}
