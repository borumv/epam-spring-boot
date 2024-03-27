package org.vlasevsky.gym.dao;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TraineeRepository extends BaseAbstractDAO<Long, Trainee> {

    public TraineeRepository() {
        super(Trainee.class);
    }

    public Optional<Trainee> findByUsername(String username) {
        @Cleanup Session session = sessionFactory.openSession();
        return session.createQuery("SELECT t FROM Trainee t WHERE t.username = :username", Trainee.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public List<Training> findTraineeTrainingsByUsernameAndCriteria(
            String username, Date fromDate, Date toDate, String trainerName, String trainingType) {
        @Cleanup Session session = sessionFactory.openSession();
        return session.createQuery(
                        "SELECT t FROM Training t JOIN t.trainee trn JOIN t.trainer tr WHERE trn.username = :username " +
                                "AND t.date BETWEEN :fromDate AND :toDate AND tr.firstName LIKE :trainerName AND t.trainingType = :trainingType", Training.class)
                .setParameter("username", username)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("trainerName", "%" + trainerName + "%")
                .setParameter("trainingType", trainingType)
                .getResultList();
    }
}