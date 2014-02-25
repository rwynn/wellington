package org.github.rwynn.wellington.persistence.audit;


import org.github.rwynn.wellington.utils.SecurityContextUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class AuditListener {

    @PrePersist
    public void auditCreate(Object entity) {
        if (entity instanceof Auditing) {
            String currentUser = SecurityContextUtils.getUsername();
            Auditing auditing = (Auditing) entity;
            Audit audit = new Audit();
            audit.setCreatedBy(currentUser);
            audit.setCreatedDate(new Date());
            audit.setLastModifiedBy(currentUser);
            audit.setLastModifiedDate(new Date());
            auditing.setAudit(audit);
        }
    }

    @PreUpdate
    public void auditUpdate(Object entity) {
        if (entity instanceof Auditing) {
            String currentUser = SecurityContextUtils.getUsername();
            Auditing auditing = (Auditing) entity;
            Audit audit = auditing.getAudit();
            audit.setLastModifiedBy(currentUser);
            audit.setLastModifiedDate(new Date());
        }
    }
}
