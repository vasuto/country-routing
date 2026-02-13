package com.cr.countryrouting;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableCaching
public class ApplicationConfiguration {

  @Bean
  public WebClient.Builder webClientBuilder() {
      return WebClient.builder();
  }

  @Bean
  public CacheManager cacheManager() {
      return new ConcurrentMapCacheManager("countriesCache");
  }

}
