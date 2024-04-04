package org.vlasevsky.gym.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Training;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TraineeRepositoryTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Trainee> traineeQuery;

    @Mock
    private Query<Training> trainingQuery;

    @InjectMocks
    private TraineeRepository traineeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(trainingQuery);
    }

    @Test
    void testFindByUsername() {
        String username = "testUsername";
        Trainee expectedTrainee = new Trainee();
        expectedTrainee.setUsername(username);

        when(traineeQuery.setParameter("username", username)).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResultOptional()).thenReturn(Optional.of(expectedTrainee));

        Optional<Trainee> result = traineeRepository.findByUsername(username);

        assertEquals(expectedTrainee, result.orElse(null));
    }

    @Test
    void testFindTraineeTrainingsByUsernameAndCriteria() {
        String username = "testUsername";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "John";
        String trainingType = "CARDIO";
        List<Training> expectedTrainings = Arrays.asList(new Training());

        when(trainingQuery.setParameter("username", username)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("fromDate", fromDate)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("toDate", toDate)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("trainerName", "%" + trainerName + "%")).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("trainingType", trainingType)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(expectedTrainings);

        List<Training> result = traineeRepository.findTraineeTrainingsByUsernameAndCriteria(
                username, fromDate, toDate, trainerName, trainingType);

        assertEquals(expectedTrainings, result);
    }
}
