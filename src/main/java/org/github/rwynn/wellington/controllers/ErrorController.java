package org.github.rwynn.wellington.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.github.rwynn.wellington.utils.SecurityContextUtils;
import org.springframework.boot.actuate.web.BasicErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;
import java.util.UUID;

@Controller
public class ErrorController extends BasicErrorController {

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public Map<String, Object> extract(RequestAttributes attributes, boolean trace, boolean log) {
        Map<String, Object> map = super.extract(attributes, true, false);
        String correlationId = UUID.randomUUID().toString();
        map.put("correlationId", correlationId);
        logError(map);
        return map;
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
