package com.astroway.catalog.repository;

import com.astroway.catalog.model.DarkSkySpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DarkSkySpotRepository extends JpaRepository<DarkSkySpot, Long> {
    List<DarkSkySpot> findByBortleScaleLessThanEqual(Integer maxBortleScale);
}