package org.github.rwynn.wellington.rest;

import org.github.rwynn.wellington.transfer.FilterDTO;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class FilterHandlerArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String FILTER_PARAM_NAME = "filter";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return FilterDTO.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return new FilterDTO(webRequest.getParameter(FILTER_PARAM_NAME));
    }
}
