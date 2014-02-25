package org.github.rwynn.wellington.services;

import org.springframework.dao.DataAccessException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface DataErrorService {
    Map<String, String> getError(HttpServletRequest request, DataAccessException ex);
}
