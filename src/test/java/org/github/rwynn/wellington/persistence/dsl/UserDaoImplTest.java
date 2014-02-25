package org.github.rwynn.wellington.persistence.dsl;

import org.github.rwynn.wellington.properties.DatabaseProperties;
import org.github.rwynn.wellington.rest.RESTPage;
import org.github.rwynn.wellington.transfer.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class UserDaoImplTest {

    @Mock
    JdbcTemplate jdbcTemplate;

    @Mock
    DatabaseProperties databaseProperties;

    @InjectMocks
    UserDaoImpl userDao = new UserDaoImpl();

    @Before
    public void init() {
        Mockito.when(databaseProperties.getJooqDialect()).thenReturn("POSTGRES");
    }

    @Test
    public void testGetUsersWithAuthority() throws SQLException {

        String expectedQuery = "select \"public\".\"users\".\"username\", \"public\".\"users\".\"locked\", \"public\".\"authorities\".\"authority\", count(\"public\".\"users\".\"username\") over () as \"count\" from \"public\".\"users\" join \"public\".\"authorities\" on \"public\".\"users\".\"username\" = \"public\".\"authorities\".\"username\" where \"public\".\"authorities\".\"authority\" = 'ROLE_ADMIN' order by \"public\".\"users\".\"username\" asc limit 5 offset 0";
        Pageable pageable = Mockito.mock(Pageable.class);

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(pageable.getPageNumber()).thenReturn(0);
        Mockito.when(pageable.getPageSize()).thenReturn(5);

        Mockito.when(resultSet.getInt(PagingConstants.COUNT)).thenReturn(1);
        Mockito.when(resultSet.getString("username")).thenReturn("admin");

        RESTPage<UserDTO> page = userDao.getUsersWithAuthority("ROLE_ADMIN", pageable);

        ArgumentCaptor<PagingRowCallbackHandler> pagingRowCallbackHandlerCaptor = ArgumentCaptor.forClass(PagingRowCallbackHandler.class);

        Mockito.verify(jdbcTemplate).query(Matchers.eq(expectedQuery), pagingRowCallbackHandlerCaptor.capture());

        PagingRowCallbackHandler pagingRowCallbackHandler = pagingRowCallbackHandlerCaptor.getValue();

        pagingRowCallbackHandler.processRow(resultSet);

        Mockito.verify(resultSet).getInt(PagingConstants.COUNT);

        Mockito.verify(resultSet).getString("username");

        Assert.assertEquals(1, page.getContentSize());
        Assert.assertEquals("admin", page.getContent().get(0).getUsername());

    }

    @Test
    public void testGetUsersWithAuthority_page2() {

        String expectedQuery = "select \"public\".\"users\".\"username\", \"public\".\"users\".\"locked\", \"public\".\"authorities\".\"authority\", count(\"public\".\"users\".\"username\") over () as \"count\" from \"public\".\"users\" join \"public\".\"authorities\" on \"public\".\"users\".\"username\" = \"public\".\"authorities\".\"username\" where \"public\".\"authorities\".\"authority\" = 'ROLE_ADMIN' order by \"public\".\"users\".\"username\" asc limit 5 offset 5";
        Pageable pageable = Mockito.mock(Pageable.class);

        Mockito.when(pageable.getPageNumber()).thenReturn(1);
        Mockito.when(pageable.getPageSize()).thenReturn(5);

        userDao.getUsersWithAuthority("ROLE_ADMIN", pageable);

        Mockito.verify(jdbcTemplate).query(Matchers.eq(expectedQuery), Matchers.any(PagingRowCallbackHandler.class));

    }
}
