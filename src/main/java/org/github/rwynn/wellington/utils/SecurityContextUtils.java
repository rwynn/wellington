package org.github.rwynn.wellington.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

public class SecurityContextUtils {

    public static String getUsername() {
        if (isAuthenticated())  {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            String username = null;
            Object principal = securityContext.getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails user =  (UserDetails) principal;
                username = user.getUsername();
            } else {
                username = principal.toString();
            }
            return username;
        }
        return null;
    }

    public static Set<String> getAuthorities() {
        Set<String> authorities = new HashSet<String>();
        if (isAuthenticated()) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            for (GrantedAuthority authority: securityContext.getAuthentication().getAuthorities()) {
                authorities.add(authority.getAuthority());
            }
        }
        return authorities;
    }

    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication() != null && securityContext.getAuthentication().isAuthenticated();
    }
}
