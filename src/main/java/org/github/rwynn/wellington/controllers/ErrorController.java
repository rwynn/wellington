package org.github.rwynn.wellington.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.github.rwynn.wellington.utils.SecurityContextUtils;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;
import java.util.UUID;

@Controller
public class ErrorController extends BasicErrorController {

    protected final Log logger = LogFactory.getLog(getClass());

    public static class LoggingErrorAttributes extends DefaultErrorAttributes {

        protected final Log logger = LogFactory.getLog(getClass());

        @Override
        public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
            Map<String, Object> attrs = super.getErrorAttributes(requestAttributes, includeStackTrace);
            String correlationId = UUID.randomUUID().toString();
            attrs.put("correlationId", correlationId);
            logError(attrs);
            return attrs;
        }

        protected void logError(Map<String, Object> attributes) {
            String correlationId = (String) attributes.get("correlationId");
            String user = SecurityContextUtils.getUsername();
            Integer status = (Integer) attributes.get("status");
            String message = (String) attributes.get("message");
            String exception = (String) attributes.get("exception");
            String trace = (String) attributes.get("trace");
            logger.error(String.format("%s|%s|%s|%s|%s|%s", user, correlationId,
                    status, message, exception, trace));
        }
    }

    public ErrorController() {
        super(new LoggingErrorAttributes());
    }

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

}
