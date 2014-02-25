package org.github.rwynn.wellington.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.github.rwynn.wellington.persistence.Authority;
import org.github.rwynn.wellington.persistence.User;
import org.github.rwynn.wellington.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Username <%s> not found", username));
        } else {
            List<String> authorities = new ArrayList<String>();
            for (Authority authority : user.getAuthoritySet()) {
                final String authorityValue = authority.getAuthorityId().getAuthority();
                authorities.add(authorityValue);
            }
            return new UserDetailsImpl(user.getUsername(), user.getKey(), authorities, user.isLocked());
        }
    }
}
