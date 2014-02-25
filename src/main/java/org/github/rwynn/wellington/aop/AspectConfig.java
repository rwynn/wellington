package org.github.rwynn.wellington.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.github.rwynn.wellington.services.MessageService;
import org.github.rwynn.wellington.transfer.UserDTO;

@Configuration
public class AspectConfig {

    @Bean
    public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
        return new AnnotationAwareAspectJAutoProxyCreator();
    }

    @Aspect
    @Component
    public static class UserAspect {

        protected final Log logger = LogFactory.getLog(getClass());

        @Autowired
        MessageService messageService;

        @Pointcut("execution(* saveUser(..))")
        private void saveUser() {}

        @Pointcut("within(org.github.rwynn.wellington.services..*)")
        private void inServices() {}

        @Pointcut("saveUser() && inServices()")
        private void saveUserService() {}

        @AfterReturning(pointcut = "saveUserService()", returning = "userDTO")
        public void sendUserCreatedMessage(UserDTO userDTO) {
            logger.info(String.format("UserAspect detected new user %s",
                    userDTO.getUsername()));
            messageService.registrationCompleted(userDTO.getUsername());
        }

    }
}
