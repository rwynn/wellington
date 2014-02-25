package org.github.rwynn.wellington.persistence;

import org.github.rwynn.wellington.persistence.audit.Audit;
import org.github.rwynn.wellington.persistence.audit.AuditListener;
import org.github.rwynn.wellington.persistence.audit.Auditing;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authorities")
@EntityListeners(AuditListener.class)
public class Authority implements Serializable, Auditing {

    @EmbeddedId
    private AuthorityId authorityId;

    @Embedded
    private Audit audit;

    @Embeddable
    public static class AuthorityId implements Serializable {

        @NotEmpty
        private String username;

        @NotEmpty
        private String authority;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            AuthorityId that = (AuthorityId) o;

            if (authority != null ? !authority.equals(that.authority) : that.authority != null) {
                return false;
            }
            if (username != null ? !username.equals(that.username) : that.username != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = username != null ? username.hashCode() : 0;
            result = 31 * result + (authority != null ? authority.hashCode() : 0);
            return result;
        }
    }

    public AuthorityId getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(AuthorityId authorityId) {
        this.authorityId = authorityId;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Authority authority = (Authority) o;

        if (audit != null ? !audit.equals(authority.audit) : authority.audit != null) {
            return false;
        }
        if (authorityId != null ? !authorityId.equals(authority.authorityId) : authority.authorityId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = authorityId != null ? authorityId.hashCode() : 0;
        result = 31 * result + (audit != null ? audit.hashCode() : 0);
        return result;
    }
}
