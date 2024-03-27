package org.vlasevsky.gym.interceptor;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class TransactionInterceptor {
    private final SessionFactory sessionFactory;

}
