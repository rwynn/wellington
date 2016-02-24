package org.github.rwynn.wellington.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EhCacheBasedUserCache ehCacheBasedUserCache;

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Value("${info.app.name}")
    String realm = "Realm";


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests().antMatchers("/webjars/**", "/css/**", "/js/**",
                "/partials/**", "/users/register", "/users/auth").permitAll();

        secureManagmentEndpoints(http);

        http.authorizeRequests().anyRequest().fullyAuthenticated();

        http.formLogin()
                .loginPage("/login").failureUrl("/login?error=true").permitAll();

        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout=true").permitAll();

    }

    private void secureManagmentEndpoints(HttpSecurity http) throws Exception {
        final String securePath = "/users/admin/**";

        http
                .authorizeRequests()
                .antMatchers((HttpMethod) null, securePath)
                .hasRole(RoleConstants.ADMIN_ROLE);

        List<Endpoint<?>> endpoints = findEndpointBeans();
        for (Endpoint<?> endPoint: endpoints) {
            if (endPoint.isSensitive()) {
                http
                        .authorizeRequests()
                        .antMatchers((HttpMethod) null, endPoint.getId()).hasRole(RoleConstants.ADMIN_ROLE);
                logger.info("Secured endpoint at path " + endPoint.getId());
            }
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                String encryptedPassword = passwordEncryptor.encryptPassword(rawPassword.toString());
                return encryptedPassword;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                return passwordEncryptor.checkPassword(rawPassword.toString(), encodedPassword);
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserCache(ehCacheBasedUserCache);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider daoAuthenticationProvider, PasswordEncoder passwordEncoder) {
        ProviderManager providerManager = new ProviderManager(Arrays.asList((AuthenticationProvider) daoAuthenticationProvider));
        return  providerManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider);
    }

    private List<Endpoint<?>> findEndpointBeans() {
        return new ArrayList(BeanFactoryUtils.beansOfTypeIncludingAncestors(
                this.applicationContext, Endpoint.class).values());
    }
}
