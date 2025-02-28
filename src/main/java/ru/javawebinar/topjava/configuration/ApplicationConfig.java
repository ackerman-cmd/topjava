package ru.javawebinar.topjava.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.CacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

@Configuration
@Import({JpaConfig.class, JdbcConfig.class, HsqldbConfig.class })
@ComponentScan(basePackages = "ru.javawebinar.topjava.service")
public class ApplicationConfig {

    @Bean
    public CacheManager cacheManager() {
        CachingProvider provider = Caching.getCachingProvider();
        javax.cache.CacheManager jCacheManager = provider.getCacheManager();

        MutableConfiguration<Object, Object> config = new MutableConfiguration<>()
                .setStoreByValue(false);

        jCacheManager.createCache("users", config);
        return new org.springframework.cache.jcache.JCacheCacheManager(jCacheManager);
    }







}
