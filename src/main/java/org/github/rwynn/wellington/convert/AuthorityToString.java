package org.github.rwynn.wellington.convert;

import org.dozer.CustomConverter;
import org.github.rwynn.wellington.persistence.Authority;


public class AuthorityToString implements CustomConverter {

    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        if (sourceFieldValue instanceof  Authority) {
            return ((Authority) sourceFieldValue).getAuthorityId().getAuthority();
        }  else {
            Authority authority = new Authority();
            Authority.AuthorityId authorityId = new Authority.AuthorityId();
            authorityId.setAuthority((String) sourceFieldValue);
            authority.setAuthorityId(authorityId);
            return authority;
        }
    }
}
