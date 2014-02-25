package org.github.rwynn.wellington.rest;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.github.rwynn.wellington.controllers.ErrorController;
import org.github.rwynn.wellington.exception.BusinessException;
import org.github.rwynn.wellington.exception.ValidationException;
import org.github.rwynn.wellington.services.DataErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RESTErrorHandler {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    DataErrorService dataErrorService;

    @ExceptionHandler(value = DataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RESTResult<Object> handleDataAccessException(DataAccessException ex,
        HttpServletRequest request, HttpServletResponse response) {
        String correlationId = logError(request, ex, HttpStatus.BAD_REQUEST);
        Map<String, String> dataError = dataErrorService.getError(request, ex);
        RESTResult<Object> objectRESTResult = new RESTResult<Object>(null, null, Arrays.asList(dataError));
        objectRESTResult.setCorrelationId(correlationId);
        return objectRESTResult;
    }

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public RESTResult<Object> handleAuthenticationException(AuthenticationException ex,
                                                        HttpServletRequest request, HttpServletResponse response) {
        String correlationId = logError(request, ex, HttpStatus.UNAUTHORIZED);
        Map<String, String> errors = new HashMap<String, String>();
        errors.put("", "Unauthorized");
        RESTResult<Object> objectRESTResult = new RESTResult<Object>(null, null, Arrays.asList(errors));
        objectRESTResult.setCorrelationId(correlationId);
        return objectRESTResult;
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RESTResult<Object> handleValidationException(ValidationException ex,
        HttpServletRequest request, HttpServletResponse response) {
        String correlationId = logError(request, ex, HttpStatus.BAD_REQUEST);
        RESTResult<Object> objectRESTResult = new RESTResult<Object>(null, ex.getErrors(), null);
        objectRESTResult.setCorrelationId(correlationId);
        return objectRESTResult;
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public RESTResult<Object> handleBusinessException(BusinessException ex,
        HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> errors = new HashMap<String, String>();
        errors.put(ex.getCode(), ex.getMessage());
        String correlationId = logError(request, ex, HttpStatus.BAD_REQUEST);
        RESTResult<Object> objectRESTResult = new RESTResult<Object>(null, null, Arrays.asList(errors));
        objectRESTResult.setCorrelationId(correlationId);
        return objectRESTResult;
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RESTResult<Object> handleInvalidJsonInputException(HttpMessageNotReadableException ex,
        HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> errors = new HashMap<String, String>();
        errors.put("", "Malformed Input");
        String correlationId = logError(request, ex, HttpStatus.BAD_REQUEST);
        RESTResult<Object> objectRESTResult = new RESTResult<Object>(null, null, Arrays.asList(errors));
        objectRESTResult.setCorrelationId(correlationId);
        return objectRESTResult;
    }

    @ExceptionHandler(value = PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RESTResult<Object> handlePropertyReferenceException(PropertyReferenceException ex,
        HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> errors = new HashMap<String, String>();
        errors.put("", ex.getMessage());
        String correlationId = logError(request, ex, HttpStatus.BAD_REQUEST);
        RESTResult<Object> objectRESTResult = new RESTResult<Object>(null, null, Arrays.asList(errors));
        objectRESTResult.setCorrelationId(correlationId);
        return objectRESTResult;
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RESTResult<Object> handleRuntimeException(RuntimeException ex,
        HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> errors = new HashMap<String, String>();
        errors.put("", ex.getMessage());
        String correlationId = logError(request, ex, HttpStatus.BAD_REQUEST);
        RESTResult<Object> objectRESTResult = new RESTResult<Object>(null, null, Arrays.asList(errors));
        objectRESTResult.setCorrelationId(correlationId);
        return objectRESTResult;
    }

    protected String logError(HttpServletRequest request, Throwable throwable, HttpStatus status) {
        request.setAttribute("javax.servlet.error.exception", throwable);
        request.setAttribute("javax.servlet.error.status_code", status.value());
        ErrorController errorController = new ErrorController();
        Map<String, Object> map = errorController.error(request);
        request.removeAttribute("javax.servlet.error.exception");
        request.removeAttribute("javax.servlet.error.status_code");
        return (String) map.get("correlationId");
    }

}
