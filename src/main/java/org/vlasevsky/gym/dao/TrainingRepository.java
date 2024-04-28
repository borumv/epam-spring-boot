package org.vlasevsky.gym.dao;

import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Training;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TrainingRepository extends BaseAbstractDAO<Long, Training> {
    public TrainingRepository() {
        super(Training.class);
    }


    public List<Training> findTrainingsByTraineeAndPeriodAndTrainer(String traineeUsername, LocalDateTime fromDate, LocalDateTime toDate, String trainerName) {
        StringBuilder queryStr = new StringBuilder("SELECT t FROM Training t WHERE t.trainee.username = :traineeUsername");

        if (fromDate != null && toDate != null) {
            queryStr.append(" AND t.date >= :fromDate AND t.date <= :toDate");
        }

        if (trainerName != null && !trainerName.isEmpty()) {
            queryStr.append(" AND t.trainer.firstName LIKE :trainerName OR t.trainer.lastName LIKE :trainerName");
        }

        var query = getCurrentSession().createQuery(queryStr.toString(), Training.class);
        query.setParameter("traineeUsername", traineeUsername);

        if (fromDate != null && toDate != null) {
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
        }

        if (trainerName != null && !trainerName.isEmpty()) {
            query.setParameter("trainerName", "%" + trainerName + "%");
        }

        return query.getResultList();

    }


    public List<Training> findTrainingsByTrainerAndPeriodAndTrainee(String trainerUsername, LocalDateTime fromDate, LocalDateTime toDate, String traineeName) {
        StringBuilder queryStr = new StringBuilder("SELECT t FROM Training t WHERE t.trainer.username = :trainerUsername");

        if (fromDate != null && toDate != null) {
            queryStr.append(" AND t.date >= :fromDate AND t.date <= :toDate");
        }

        if (traineeName != null && !traineeName.isEmpty()) {
            queryStr.append(" AND t.trainee.firstName LIKE :traineeName OR t.trainee.lastName LIKE :traineeName");
        }

        var query = getCurrentSession().createQuery(queryStr.toString(), Training.class);
        query.setParameter("trainerUsername", trainerUsername);

        if (fromDate != null && toDate != null) {
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
        }

        if (traineeName != null && !traineeName.isEmpty()) {
            query.setParameter("trainerName", "%" + traineeName + "%");
        }

        return query.getResultList();

    }
}

