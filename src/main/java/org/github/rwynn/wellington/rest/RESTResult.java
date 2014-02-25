package org.github.rwynn.wellington.rest;

import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Map;

public class RESTResult<T> {

    private T results;

    private List<ObjectError> validationErrors;

    private List<Map<String, String>> dataErrors;

    private String correlationId;

    public RESTResult(T results, List<ObjectError> validationErrors) {
        this.results = results;
        this.validationErrors = validationErrors;
    }

    public RESTResult(T results, List<ObjectError> validationErrors, List<Map<String, String>> dataErrors) {
        this.results = results;
        this.dataErrors = dataErrors;
        this.validationErrors = validationErrors;
    }

    public RESTResult(T results) {
        this.results = results;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public List<ObjectError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ObjectError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<Map<String, String>> getDataErrors() {
        return dataErrors;
    }

    public void setDataErrors(List<Map<String, String>> dataErrors) {
        this.dataErrors = dataErrors;
    }

    public boolean getSuccess() {
        boolean success = false;
        boolean noValidationErrors = this.validationErrors == null || this.validationErrors.isEmpty();
        boolean noDataErrors = this.dataErrors == null || this.dataErrors.isEmpty();
        if (noValidationErrors && noDataErrors) {
            success = true;
        }
        return success;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
