package org.vlasevsky.gym.dao;

import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class TrainerRepository extends BaseAbstractDAO<Long, Trainer> {
    public TrainerRepository() {
        super(Trainer.class);
    }

    public Optional<Trainer> findByUsername(String username) {
        return getCurrentSession().createQuery("SELECT t FROM Trainer t WHERE t.username = :username", Trainer.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public List<Trainer> findTrainersNotAssignedToTrainee(String traineeUsername) {
        return getCurrentSession().createQuery(
                        "SELECT t FROM Trainer t WHERE t NOT IN " +
                                "(SELECT tr.trainers FROM Trainee tr WHERE tr.username = :traineeUsername)", Trainer.class)
                .setParameter("traineeUsername", traineeUsername)
                .getResultList();
    }


    public List<Trainer> findAllTrainersByUsername(List<String> names) {
        return getCurrentSession().createQuery("SELECT t FROM Trainer t WHERE t.username IN (:names)", Trainer.class)
                .setParameter("names", names)
                .getResultList();
    }
}
