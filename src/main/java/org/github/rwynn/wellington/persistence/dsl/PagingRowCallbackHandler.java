package org.github.rwynn.wellington.persistence.dsl;

import org.github.rwynn.wellington.rest.RESTPage;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PagingRowCallbackHandler implements RowCallbackHandler {

    protected RowCallbackHandler rowCallbackHandler;

    protected RESTPage<?> restPage;

    protected Pageable pageable;

    boolean pageAttributesSet = false;

    public PagingRowCallbackHandler(RESTPage<?> restPage,
                                    Pageable pageable,
                                    RowCallbackHandler rowCallbackHandler) {
        this.rowCallbackHandler = rowCallbackHandler;
        this.restPage = restPage;
        this.pageable = pageable;

        this.restPage.setPageSize(this.pageable.getPageSize());
        this.restPage.setPageNumber(this.pageable.getPageNumber());
        this.restPage.setHasNextPage(false);
        this.restPage.setHasPreviousPage(false);
        this.restPage.setFirstPage(false);
        this.restPage.setLastPage(false);
        this.restPage.setContentSize(0);
        this.restPage.setHasContent(false);
        this.restPage.setTotalPages(0);
        this.restPage.setTotalSize(0);
    }

    @Override
    public void processRow(ResultSet rs) throws SQLException {
          setPageAttributes(rs);
          if (this.rowCallbackHandler != null) {
              this.rowCallbackHandler.processRow(rs);
          }
    }

    protected final void setPageAttributes(ResultSet resultSet) throws SQLException {
        if (!this.pageAttributesSet) {
            this.restPage.setHasContent(this.restPage.getContentSize() > 0);
            this.restPage.setTotalPages(this.restPage.getContentSize() / this.restPage.getPageSize()
                    + this.restPage.getContentSize() % this.restPage.getPageSize() > 0 ? 1 : 0);
            this.restPage.setHasPreviousPage((this.restPage.getPageNumber() + 1) < this.restPage.getTotalPages());
            this.restPage.setHasNextPage((this.restPage.getPageNumber() + 1) < this.restPage.getTotalPages());
            this.restPage.setFirstPage(this.restPage.getPageNumber() == 0);
            this.restPage.setLastPage((this.restPage.getPageNumber() + 1) == this.restPage.getTotalPages());
            this.restPage.setTotalSize(resultSet.getInt(PagingConstants.COUNT));
            this.pageAttributesSet = true;
        }
        this.restPage.setContentSize(this.restPage.getContentSize() + 1);
    }
}
