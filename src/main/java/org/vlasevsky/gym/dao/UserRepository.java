package org.vlasevsky.gym.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.User;

@Component
public class UserRepository extends BaseAbstractDAO<Long, User> {
    public UserRepository() {
        super(User.class);
    }

    public boolean existsByUsername(String username) {
        Long count = getCurrentSession().createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }


    public boolean checkCredentials(String username, String password) {
        Long count = getCurrentSession().createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username AND u.password = :password", Long.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
        return count > 0;
    }
}
