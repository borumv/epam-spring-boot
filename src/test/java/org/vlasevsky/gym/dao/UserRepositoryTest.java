package org.vlasevsky.gym.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.model.User;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Long> query;

    @InjectMocks
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery(anyString(), eq(Long.class))).thenReturn(query);
    }

    @Test
    void testExistsByUsername() {
        String username = "testUsername";
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        boolean exists = userRepository.existsByUsername(username);

        assertTrue(exists);
        verify(query).setParameter("username", username);
        verify(query).getSingleResult();
    }


}
