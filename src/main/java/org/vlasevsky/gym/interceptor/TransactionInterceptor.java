package org.vlasevsky.gym.interceptor;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;

@RequiredArgsConstructor
public class TransactionInterceptor {
    private final SessionFactory sessionFactory;

}
