package org.github.rwynn.wellington.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    private String code;

    public BusinessException(String code, String message) {
        super(message);
        setCode(code);
    }

    public BusinessException(String code, String message, Object... args) {
        super(MessageFormat.format(message, args));
        setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
