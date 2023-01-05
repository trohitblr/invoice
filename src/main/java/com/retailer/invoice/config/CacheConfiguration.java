package com.retailer.invoice.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.retailer.invoice.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.retailer.invoice.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.retailer.invoice.domain.User.class.getName());
            createCache(cm, com.retailer.invoice.domain.Authority.class.getName());
            createCache(cm, com.retailer.invoice.domain.User.class.getName() + ".authorities");
            createCache(cm, com.retailer.invoice.domain.Retailer.class.getName());
            createCache(cm, com.retailer.invoice.domain.Retailer.class.getName() + ".invoices");
            createCache(cm, com.retailer.invoice.domain.Retailer.class.getName() + ".retailerUsers");
            createCache(cm, com.retailer.invoice.domain.Retailer.class.getName() + ".retailInventories");
            createCache(cm, com.retailer.invoice.domain.Retailer.class.getName() + ".customers");
            createCache(cm, com.retailer.invoice.domain.Address.class.getName());
            createCache(cm, com.retailer.invoice.domain.Media.class.getName());
            createCache(cm, com.retailer.invoice.domain.Customer.class.getName());
            createCache(cm, com.retailer.invoice.domain.Customer.class.getName() + ".retailers");
            createCache(cm, com.retailer.invoice.domain.Invoice.class.getName());
            createCache(cm, com.retailer.invoice.domain.Invoice.class.getName() + ".invoiceLineItems");
            createCache(cm, com.retailer.invoice.domain.Invoice.class.getName() + ".invoiceGStLineItems");
            createCache(cm, com.retailer.invoice.domain.RetailerUser.class.getName());
            createCache(cm, com.retailer.invoice.domain.InvoiceLineItem.class.getName());
            createCache(cm, com.retailer.invoice.domain.InvoiceGStLineItem.class.getName());
            createCache(cm, com.retailer.invoice.domain.Catalouge.class.getName());
            createCache(cm, com.retailer.invoice.domain.Categories.class.getName());
            createCache(cm, com.retailer.invoice.domain.Categories.class.getName() + ".catalouges");
            createCache(cm, com.retailer.invoice.domain.GstSlave.class.getName());
            createCache(cm, com.retailer.invoice.domain.GstSlave.class.getName() + ".catalouges");
            createCache(cm, com.retailer.invoice.domain.RetailInventory.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
