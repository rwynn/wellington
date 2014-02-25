package org.github.rwynn.wellington.transfer;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.sql.Update;
import org.hibernate.validator.constraints.NotEmpty;
import org.github.rwynn.wellington.validation.NewUser;
import org.github.rwynn.wellington.validation.UpdateLock;
import org.github.rwynn.wellington.validation.UpdateRoles;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO implements Serializable {

    @NotEmpty(message = "{org.github.rwynn.wellington.transfer.UserDTO.username.NotEmpty.message}", groups = { NewUser.class, UpdateLock.class, UpdateRoles.class })
    private String username;

    @NotEmpty(message = "{org.github.rwynn.wellington.transfer.UserDTO.password.NotEmpty.message}", groups = NewUser.class)
    private String password;

    @NotNull(message = "{org.github.rwynn.wellington.transfer.UserDTO.locked.NotNull.message}", groups = UpdateLock.class)
    private Boolean locked;

    @NotEmpty(message = "{org.github.rwynn.wellington.transfer.UserDTO.authorities.NotEmpty.message}", groups = UpdateRoles.class)
    private Set<String> authorities = new HashSet<String>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", locked=" + locked +
                ", authorities=" + authorities +
                '}';
    }
}
