package com.astroway.catalog.controller;

import com.astroway.catalog.dto.NasaApodDto;
import com.astroway.catalog.model.DarkSkySpot;
import com.astroway.catalog.service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/spots")
    public ResponseEntity<List<DarkSkySpot>> getAllSpots() {
        return ResponseEntity.ok(catalogService.getAllSpots());
    }

    @PostMapping("/spots")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    public ResponseEntity<DarkSkySpot> createSpot(@Valid @RequestBody DarkSkySpot spot) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createSpot(spot));
    }

    @GetMapping("/nasa/apod")
    public ResponseEntity<NasaApodDto> getNasaApod() {
        return ResponseEntity.ok(catalogService.getNasaApod());
    }
}