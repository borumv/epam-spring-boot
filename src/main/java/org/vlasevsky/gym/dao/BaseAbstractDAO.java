package org.vlasevsky.gym.dao;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.vlasevsky.gym.model.BaseEntity;
import org.vlasevsky.gym.model.Trainee;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseAbstractDAO<K extends Serializable, T extends BaseEntity> implements BaseDao<K, T> {

    private final Class<T> clazz;

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Optional<T> findById(K  id) {
        @Cleanup Session session = sessionFactory.openSession();
        return Optional.ofNullable(session.find(clazz, id));

    }


    @Override
    public T save(T entity) {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        if(entity.getId() != null)
            update(entity);
        else {
            session.persist(entity);
            session.flush();

        }
        session.getTransaction().commit();

        return entity;
    }

    @Override
    public void delete(K id) {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        T entity = session.find(clazz, id);
        if (entity != null) {
            session.remove(entity);
            session.getTransaction().commit();
        } else {
            session.getTransaction().rollback();
            throw new IllegalArgumentException("Entity with id " + id + " not found");
        }
    }

    @Override
    public void update(T entity) {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.merge(entity);
        session.flush();
        session.getTransaction().commit();
    }

    @Override
    public List<T> findAll() {

        @Cleanup Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        JpaCriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);
        query.from(clazz);

        return session.createQuery(query)
                .getResultList();

    }
}
