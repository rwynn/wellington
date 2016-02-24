package org.github.rwynn.wellington.convert;

import org.dozer.CustomConverter;
import org.springframework.data.domain.Page;
import org.github.rwynn.wellington.rest.RESTPage;


public class PageConverter implements CustomConverter {

    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        if (sourceFieldValue instanceof Page) {
            Page existingPage = (Page) sourceFieldValue;
            RESTPage<Object> newPage = (RESTPage<Object>) existingDestinationFieldValue;
            newPage.setHasNextPage(existingPage.hasNext());
            newPage.setHasContent(existingPage.hasContent());
            newPage.setFirstPage(existingPage.isFirst());
            newPage.setPageNumber(existingPage.getNumber());
            newPage.setContentSize(existingPage.getNumberOfElements());
            newPage.setLastPage(existingPage.isLast());
            newPage.setTotalPages(existingPage.getTotalPages());
            newPage.setHasPreviousPage(existingPage.hasPrevious());
            newPage.setPageSize(existingPage.getSize());
            newPage.setTotalSize(existingPage.getTotalElements());
            return newPage;
        }
        return null;
    }
}
