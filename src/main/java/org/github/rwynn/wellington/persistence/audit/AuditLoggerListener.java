package org.github.rwynn.wellington.persistence.audit;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.github.rwynn.wellington.utils.SecurityContextUtils;

import javax.persistence.PostLoad;

public class AuditLoggerListener {

    protected final Log logger = LogFactory.getLog(getClass());

    @PostLoad
    public void auditLogLoad(Object entity) {
        if (entity instanceof AuditAccess) {
            AuditAccess auditAccess = (AuditAccess) entity;
            String currentUser = SecurityContextUtils.getUsername();
            logger.info(String.format("%s|PostLoad|%s|%s",
                    currentUser, entity.getClass().getSimpleName(), auditAccess.getAuditRecord()));
        }
    }
}
