package org.github.rwynn.wellington.services;


import org.github.rwynn.wellington.transfer.FilterDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.github.rwynn.wellington.rest.RESTPage;
import org.github.rwynn.wellington.transfer.UserDTO;

public interface UserService {

    public UserDTO saveUser(UserDTO user);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RESTPage<UserDTO> getUsers(FilterDTO filterDTO, Pageable pageable);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RESTPage<UserDTO> getAdmins(Pageable pageable);

    @PreAuthorize("hasRole('ROLE_USER')")
    public UserDTO getUserByUsername(String username);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDTO updateLock(UserDTO user);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDTO updateRoles(UserDTO user);

}
