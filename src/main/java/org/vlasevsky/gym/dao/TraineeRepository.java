package org.vlasevsky.gym.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Slf4j
@Repository
public class TraineeRepository extends BaseAbstractDAO<Long, Trainee> {

    public TraineeRepository() {
        super(Trainee.class);
    }

    public Optional<Trainee> findByUsername(String username) {
        return getCurrentSession().createQuery("SELECT t FROM Trainee t WHERE t.username = :username", Trainee.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public List<Training> findTraineeTrainingsByUsernameAndCriteria(
            String username, Date fromDate, Date toDate, String trainerName, String trainingType) {
        return getCurrentSession().createQuery(
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