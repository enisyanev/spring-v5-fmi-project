package com.project.spring.digitalwallet.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.project.spring.digitalwallet.dto.fxrates.FxRatesApiResponse;
import com.project.spring.digitalwallet.exception.ApiCommunicationException;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class FxRatesService {

    private static final String API_ENDPOINT = "https://api.exchangeratesapi.io/latest?base=";

    private RestTemplate restTemplate;

    private LoadingCache<String, Map<String, BigDecimal>> cache = CacheBuilder.newBuilder()
        .maximumSize(100)
        .expireAfterWrite(60, TimeUnit.MINUTES)
        .build(new CacheLoader<String, Map<String, BigDecimal>>() {
            @Override
            public Map<String, BigDecimal> load(String key) {
                return callApi(key);
            }
        });

    public FxRatesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getConvertedAmount(String fromCurrency, String toCurrency,
                                         BigDecimal amount) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        Map<String, BigDecimal> unchecked = cache.getUnchecked(fromCurrency);

        Optional<Map.Entry<String, BigDecimal>> fxRate =
            unchecked.entrySet().stream().filter(x -> x.getKey().equals(toCurrency)).findFirst();

        if (fxRate.isPresent()) {
            return amount.multiply(fxRate.get().getValue());
        }

        throw new InvalidEntityDataException("No fx rate for given currency!");
    }

    private Map<String, BigDecimal> callApi(String currency) {
        URI uri = URI.create(API_ENDPOINT + currency);
        try {
            ResponseEntity<FxRatesApiResponse> response =
                restTemplate.getForEntity(uri, FxRatesApiResponse.class);
            return response.getBody().getRates();
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().is5xxServerError()) {
                throw new ApiCommunicationException("Error while communicating with fx rates API");
            }
            if (ex.getStatusCode().is4xxClientError()) {
                throw new InvalidEntityDataException("No fx rate for given currency!");
            }
        }
        return null;
    }

}
