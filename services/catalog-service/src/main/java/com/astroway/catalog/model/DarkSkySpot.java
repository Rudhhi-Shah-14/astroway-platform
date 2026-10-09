package com.astroway.catalog.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "dark_sky_spots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DarkSkySpot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "bortle_scale", nullable = false)
    private Integer bortleScale;

    @Column(name = "elevation_meters")
    private Integer elevationMeters;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}