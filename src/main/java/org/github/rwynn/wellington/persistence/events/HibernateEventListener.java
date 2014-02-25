package org.github.rwynn.wellington.persistence.events;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.github.rwynn.wellington.persistence.User;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class HibernateEventListener {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HibernateEntityManagerFactory entityManagerFactory;

    @PostConstruct
    public void registerListeners() {

        EventListenerRegistry listenerRegistry = ((SessionFactoryImpl) entityManagerFactory
                .getSessionFactory()).getServiceRegistry().getService(
                EventListenerRegistry.class);

        listenerRegistry.appendListeners(EventType.POST_COMMIT_INSERT,
                new PostInsertEventListener() {
                    @Override
                    public void onPostInsert(PostInsertEvent event) {
                        if (event.getEntity() instanceof User) {
                            User user = (User) event.getEntity();
                            logger.info(String.format("Got hibernate insert event for user %s",
                                    user.getUsername()));
                        }
                    }

                    @Override
                    public boolean requiresPostCommitHanding(EntityPersister persister) {
                        return true;
                    }
                });
    }
}
