package org.github.rwynn.wellington.services;

import org.dozer.DozerBeanMapper;
import org.github.rwynn.wellington.exception.NotFoundException;
import org.github.rwynn.wellington.persistence.Authority;
import org.github.rwynn.wellington.persistence.User;
import org.github.rwynn.wellington.persistence.UserRepository;
import org.github.rwynn.wellington.persistence.dsl.UserDao;
import org.github.rwynn.wellington.properties.BusinessMessageProperties;
import org.github.rwynn.wellington.rest.RESTPage;
import org.github.rwynn.wellington.security.RoleConstants;
import org.github.rwynn.wellington.transfer.FilterDTO;
import org.github.rwynn.wellington.transfer.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DozerBeanMapper dozerBeanMapper;

    @Autowired
    BusinessMessageProperties businessMessageProperties;

    @Autowired
    UserDao userDao;

    @Override
    @Transactional
    public RESTPage<UserDTO> getAdmins(Pageable pageable) {
        return userDao.getUsersWithAuthority(RoleConstants.ADMIN_AUTHORITY, pageable);
    }

    @Override
    @Transactional
    @CacheEvict(value = "userList", allEntries = true)
    public UserDTO saveUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setKey(passwordEncoder.encode(userDTO.getPassword()));
        user.setLocked(false);
        Set<Authority> authoritySet = getDefaultAuthorities(user);
        user.setAuthoritySet(authoritySet);
        userRepository.save(user);
        return getUserDTO(user);
    }

    private Set<Authority> getDefaultAuthorities(User user) {
        Set<Authority> authoritySet = new HashSet<Authority>();
        Authority authority = dozerBeanMapper.map(RoleConstants.USER_AUTHORITY, Authority.class);
        authority.getAuthorityId().setUsername(user.getUsername());
        authoritySet.add(authority);
        return authoritySet;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userList", key = "{ #pageable.hashCode(), #filterDTO.hashCode() }")
    public RESTPage<UserDTO> getUsers(FilterDTO filterDTO, Pageable pageable) {
        RESTPage<UserDTO> page = new RESTPage<UserDTO>();
        List<UserDTO> results = new ArrayList<UserDTO>();

        Page<User> pageOfUsers = null;
        if (filterDTO.isBlank()) {
            pageOfUsers = userRepository.findAll(null, pageable);
        } else {
            pageOfUsers = userRepository.findByUsernameLike(filterDTO.getWildcardText(), pageable);
        }

        for (User user: pageOfUsers) {
            UserDTO userDTO = getUserDTO(user);
            results.add(userDTO);
        }
        dozerBeanMapper.map(pageOfUsers, page);
        page.setContent(results);
        return page;
    }

    private UserDTO getUserDTO(User user) {
        return dozerBeanMapper.map(user, UserDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            userNotFound(userName);
        }
        return getUserDTO(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"userList", "userCache"}, allEntries = true)
    public UserDTO updateLock(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername());
        verifyUser(userDTO, user);
        user.setLocked(userDTO.isLocked());
        userRepository.save(user);
        return getUserDTO(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"userList", "userCache"}, allEntries = true)
    public UserDTO updateRoles(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername());
        verifyUser(userDTO, user);
        for (String auth: userDTO.getAuthorities()) {
            Authority authority = dozerBeanMapper.map(auth, Authority.class);
            authority.getAuthorityId().setUsername(user.getUsername());
            user.getAuthoritySet().add(authority);
        }
        userRepository.save(user);
        return getUserDTO(user);
    }

    private void verifyUser(UserDTO userDTO, User user) {
        if (user == null) {
            userNotFound(userDTO.getUsername());
        }
    }

    private void userNotFound(String userName) {
        throw new NotFoundException(businessMessageProperties.getCode(),
            businessMessageProperties.getUserNotFound(), userName);
    }
}
