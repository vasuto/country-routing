package com.cr.countryrouting;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@EnableCaching
public class CountryService {

  private WebClient webClient;

    @Value("${country.api.url}")
    private String apiUrl;

    @PostConstruct
    public void init() {
      ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                    .codecs(configurer -> configurer.defaultCodecs()
                            .maxInMemorySize(10 * 1024 * 1024)) // 10 MB limit for buffering
                    .build();
      this.webClient = WebClient.builder().baseUrl(apiUrl).exchangeStrategies(exchangeStrategies).build();
    }

    public CountryService(WebClient.Builder webClientBuilder) {
      this.webClient = webClientBuilder.build();
    }

    @Cacheable(value = "countriesCache", key = "'countries'")
    public Map<String, JsonNode> getCountries() {
      Mono<String> responseMono = this.webClient.get()
              .retrieve()
              .bodyToMono(String.class);

      return responseMono.blockOptional()
              .map(response -> {
                  ObjectMapper objectMapper = new ObjectMapper();
                  JsonNode rootNode = objectMapper.readTree(response);

                  Map<String, JsonNode> countries = new HashMap<>();
                  rootNode.forEach(country -> {
                      String cca3 = country.get("cca3").asString();
                      countries.put(cca3, country);
                  });
                  return countries;
              })
              .orElseThrow(() -> new RuntimeException("Failed to fetch country data"));
    }

}
