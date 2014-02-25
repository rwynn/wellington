package org.github.rwynn.wellington.persistence.dsl;


import org.github.rwynn.wellington.rest.RESTPage;
import org.github.rwynn.wellington.transfer.UserDTO;
import org.springframework.data.domain.Pageable;

public interface UserDao {

    public RESTPage<UserDTO> getUsersWithAuthority(String authority, Pageable pageable);

    public RESTPage<UserDTO> searchUsersByUsername(String username, Pageable pageable);
}
