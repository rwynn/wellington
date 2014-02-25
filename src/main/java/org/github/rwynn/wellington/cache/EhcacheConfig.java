package org.github.rwynn.wellington.cache;

import net.sf.ehcache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;

@Configuration
public class EhcacheConfig {

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setCacheManagerName("CACHE_MANAGER");
        ehCacheManagerFactoryBean.setShared(true);
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean ehCacheManagerFactoryBean) {
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager(ehCacheManagerFactoryBean.getObject());
        return ehCacheCacheManager;
    }

    @Bean
    public EhCacheBasedUserCache ehCacheBasedUserCache(EhCacheCacheManager ehCacheCacheManager) {
        EhCacheBasedUserCache ehCacheBasedUserCache = new EhCacheBasedUserCache();
        ehCacheBasedUserCache.setCache((Cache) ehCacheCacheManager.getCache("userCache").getNativeCache());
        return ehCacheBasedUserCache;
    }

}
