package org.vlasevsky.gym.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Cleanup;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.User;

@Component
public class UserRepository extends BaseAbstractDAO<Long, User> {
    public UserRepository() {
        super(User.class);
    }

    public boolean existsByUsername(String username) {
        @Cleanup Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(criteriaBuilder.count(root))
                .where(criteriaBuilder.equal(root.get("username"), username));

        Long count = session.createQuery(criteriaQuery).getSingleResult();
        return count > 0;
    }


    public boolean checkCredentials(String username, String password) {
        @Cleanup Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(criteriaBuilder.count(root))
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("username"), username),
                        criteriaBuilder.equal(root.get("password"), password)
                ));

        Long count = session.createQuery(criteriaQuery).getSingleResult();
        return count > 0;
    }
}
