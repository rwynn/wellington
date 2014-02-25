package org.github.rwynn.wellington.services;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.github.rwynn.wellington.convert.DozerConfig;
import org.github.rwynn.wellington.persistence.User;
import org.github.rwynn.wellington.persistence.UserRepository;
import org.github.rwynn.wellington.properties.BusinessMessageProperties;
import org.github.rwynn.wellington.security.SecurityConfig;
import org.github.rwynn.wellington.transfer.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService = new UserServiceImpl();

    @Before
    public void setup() throws Exception {
        DozerConfig dozerConfig = new DozerConfig();
        DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = dozerConfig.dozerBeanMapperFactoryBean();
        dozerBeanMapperFactoryBean.afterPropertiesSet();
        userService.dozerBeanMapper = dozerConfig.dozerBeanMapper(dozerBeanMapperFactoryBean);

        SecurityConfig securityConfig = new SecurityConfig();
        userService.passwordEncoder = securityConfig.passwordEncoder();

        BusinessMessageProperties businessMessageProperties = new BusinessMessageProperties();
        userService.businessMessageProperties = businessMessageProperties;

        Assert.assertNotNull(userService.passwordEncoder);
        Assert.assertNotNull(userService.businessMessageProperties);
        Assert.assertNotNull(userService.dozerBeanMapper);
    }

    @Test
    public void testSaveUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user");
        userDTO.setPassword("pass");
        UserDTO saved = userService.saveUser(userDTO);
        Assert.assertFalse(saved.isLocked());
        Assert.assertFalse(saved.getAuthorities().isEmpty());
        Assert.assertEquals(1, saved.getAuthorities().size());
        Assert.assertEquals("ROLE_USER", saved.getAuthorities().iterator().next());
        Assert.assertEquals("user", saved.getUsername());

        ArgumentCaptor<User> persisted = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(persisted.capture());
        User persistedArg = persisted.getValue();
        Assert.assertEquals("user", persistedArg.getUsername());
        Assert.assertNotEquals("pass", persistedArg.getKey());
        Assert.assertEquals(1, persistedArg.getAuthoritySet().size());
        Assert.assertFalse(persistedArg.isLocked());

    }
}
