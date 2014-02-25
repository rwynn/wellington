package org.github.rwynn.wellington.persistence;


import org.github.rwynn.wellington.persistence.audit.AuditAccess;
import org.github.rwynn.wellington.persistence.audit.AuditLoggerListener;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "users")
@EntityListeners(AuditLoggerListener.class)
public class User implements Serializable, AuditAccess {

    @Id
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    private Long id;

    @Version
    private Long version;

    @Column
    @NotEmpty
    private String username;

    @Column
    @NotEmpty
    private String key;

    @Column
    private boolean locked;

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private Set<Authority> authoritySet;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Authority> getAuthoritySet() {
        return authoritySet;
    }

    public void setAuthoritySet(Set<Authority> authoritySet) {
        this.authoritySet = authoritySet;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getAuditRecord() {
        return getUsername();
    }
}
