package com.astroway.catalog.service;

import com.astroway.catalog.dto.NasaApodDto;
import com.astroway.catalog.model.DarkSkySpot;
import com.astroway.catalog.repository.DarkSkySpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final DarkSkySpotRepository spotRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${application.nasa.api-key}")
    private String nasaApiKey;

    @Value("${application.nasa.apod-url}")
    private String nasaApodUrl;

    @Cacheable(value = "darkSkySpots")
    public List<DarkSkySpot> getAllSpots() {
        log.info("Fetching dark sky spots from PostgreSQL (Cache Miss)...");
        return spotRepository.findAll();
    }

    @CacheEvict(value = "darkSkySpots", allEntries = true)
    public DarkSkySpot createSpot(DarkSkySpot spot) {
        log.info("Saving new spot and evicting darkSkySpots cache...");
        return spotRepository.save(spot);
    }

    @Cacheable(value = "nasaApod", key = "#root.methodName")
    public NasaApodDto getNasaApod() {
        log.info("Fetching NASA APOD from external API (Cache Miss)...");
        String url = nasaApodUrl + "?api_key=" + nasaApiKey;
        try {
            return restTemplate.getForObject(url, NasaApodDto.class);
        } catch (Exception e) {
            log.warn("NASA API call failed or rate-limited. Returning fallback APOD data: {}", e.getMessage());
            return NasaApodDto.builder()
                    .title("The Cosmic Web & Deep Sky")
                    .explanation("Fallback cosmic image rendered during NASA API rate limits.")
                    .url("https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86")
                    .mediaType("image")
                    .build();
        }
    }
}