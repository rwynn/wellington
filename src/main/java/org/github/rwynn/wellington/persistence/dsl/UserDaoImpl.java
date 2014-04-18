package org.github.rwynn.wellington.persistence.dsl;

import org.github.rwynn.wellington.rest.RESTPage;
import org.github.rwynn.wellington.transfer.UserDTO;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.github.rwynn.wellington.persistence.dsl.PagingConstants.*;
import static org.github.rwynn.wellington.persistence.jooq.Tables.AUTHORITIES;
import static org.github.rwynn.wellington.persistence.jooq.Tables.USERS;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.param;

@Component
public class UserDaoImpl extends JooqDao implements UserDao {

    public RESTPage<UserDTO> getUsersWithAuthority(String authority, Pageable pageable) {
        final RESTPage<UserDTO> page = new RESTPage<UserDTO>();
        page.setContent(new ArrayList<UserDTO>());

        SelectQuery query = selectUserQuery(authorityEq());

        query.bind(AUTHORITIES.AUTHORITY.getName(), authority);
        query.bind(LIMIT, pageable.getPageSize());
        query.bind(OFFSET, pageable.getPageNumber() * pageable.getPageSize());

        final Map<String, UserDTO> users = new HashMap<String, UserDTO>();

        PagingRowCallbackHandler pagingRowCallbackHandler =
                new PagingRowCallbackHandler(page, pageable, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                boolean locked = rs.getBoolean(USERS.LOCKED.getName());
                String username = rs.getString(USERS.USERNAME.getName());
                String authority = rs.getString(AUTHORITIES.AUTHORITY.getName());
                processUser(locked, username, authority, page, users);
            }
        });

        jdbcTemplate.query(query.getSQL(), pagingRowCallbackHandler);

        return page;
    }

    public RESTPage<UserDTO> searchUsersByUsername(String username, Pageable pageable) {
        final RESTPage<UserDTO> page = new RESTPage<UserDTO>();
        page.setContent(new ArrayList<UserDTO>());

        SelectQuery query = selectUserQuery(userNameLike());

        query.bind(USERS.USERNAME.getName(), username);
        query.bind(LIMIT, pageable.getPageSize());
        query.bind(OFFSET, pageable.getPageNumber() * pageable.getPageSize());

        final Map<String, UserDTO> users = new HashMap<String, UserDTO>();

        PagingRowCallbackHandler pagingRowCallbackHandler =
                new PagingRowCallbackHandler(page, pageable, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                boolean locked = rs.getBoolean(USERS.LOCKED.getName());
                String username = rs.getString(USERS.USERNAME.getName());
                String authority = rs.getString(AUTHORITIES.AUTHORITY.getName());
                processUser(locked, username, authority, page, users);
            }
        });

        jdbcTemplate.query(query.getSQL(), pagingRowCallbackHandler);

        return page;
    }

    protected SelectQuery<Record4<String, Boolean, String, Integer>>
        selectUserQuery(Condition condition) {
        DSLContext context = getDslContext();
        return context.select(USERS.USERNAME, USERS.LOCKED, AUTHORITIES.AUTHORITY,
                count(USERS.USERNAME).over().as(COUNT)).
                from(USERS).
                join(AUTHORITIES).
                on(USERS.USERNAME.eq(AUTHORITIES.USERNAME)).
                where(condition).
                orderBy(USERS.USERNAME.asc()).
                limit(DSL.param(LIMIT, Integer.class)).
                offset(DSL.param(OFFSET, Integer.class)).
                getQuery();
    }

    protected Condition authorityEq() {
        return AUTHORITIES.AUTHORITY.eq(param(AUTHORITIES.AUTHORITY.getName(), String.class));
    }

    protected Condition userNameLike() {
        return USERS.USERNAME.likeIgnoreCase(param(USERS.USERNAME.getName(), String.class));
    }

    protected final void processUser(boolean locked, String username, String authority,
                                     RESTPage<UserDTO> page, Map<String, UserDTO> users) {
        UserDTO userDTO = users.get(username);
        if (userDTO != null) {
            userDTO.getAuthorities().add(authority);
        } else {
            userDTO = new UserDTO();
            userDTO.setAuthorities(new HashSet<String>());
            userDTO.setUsername(username);
            userDTO.setLocked(locked);
            userDTO.getAuthorities().add(authority);
            page.getContent().add(userDTO);
            users.put(username, userDTO);
        }
    }
}
