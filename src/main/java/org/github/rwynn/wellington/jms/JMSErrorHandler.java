package org.github.rwynn.wellington.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class JMSErrorHandler implements ErrorHandler {

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public void handleError(Throwable t) {
        logger.error("Error Handling JMS message", t);
    }
}
