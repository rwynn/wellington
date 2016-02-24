package org.github.rwynn.wellington.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataErrorServiceImpl implements DataErrorService {

    @Autowired
    YamlMapFactoryBean errorConfiguration;

    static final String UNMAPPED = "unmapped";
    static final String CODE = "code";
    static final String MESSAGE = "message";
    static final String REQUESTS = "requests";
    static final String DEFAULT_MESSAGE = "defaultMessage";

    @Override
    public Map<String, String> getError(HttpServletRequest request, DataAccessException ex) {
        Throwable sqlException = ex;
        while (sqlException instanceof SQLException == false) {
            sqlException = sqlException.getCause();
            if (sqlException == null) {
                break;
            }
        }
        return createErrorMap(request, sqlException);
    }

    private Map<String, String> createErrorMap(HttpServletRequest request, Throwable sqlException) {
        Map<String, Object> base = errorConfiguration.getObject();
        Map<String, String> unmapped = (Map<String, String>) base.get(UNMAPPED);
        String sqlState = unmapped.get(CODE);
        if (sqlException instanceof SQLException) {
            sqlState = ((SQLException) sqlException).getSQLState();
        }
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put(DEFAULT_MESSAGE, getMessageText(request, sqlState));
        return errorMap;
    }

    private String getMessageText(HttpServletRequest request, String sqlState) {
        String message = null;
        Map<String, Object> base = errorConfiguration.getObject();
        Map<String, Object> requests = (Map<String, Object>) base.get(REQUESTS);
        Map<String, Object> method = (Map<String, Object>) requests.get(request.getMethod().toUpperCase());
        if (method != null) {
            Map<String, Object> uri = (Map<String, Object>) method.get(request.getRequestURI());
            if (uri != null) {
                message = (String) uri.get(sqlState);
            }
        }
        if (message == null) {
            Map<String, String> unmapped = (Map<String, String>) base.get(UNMAPPED);
            message = unmapped.get(MESSAGE);
        }
        return message;
    }
}
