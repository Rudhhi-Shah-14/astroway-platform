CREATE TABLE IF NOT EXISTS dark_sky_spots (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    bortle_scale INT NOT NULL CHECK (bortle_scale BETWEEN 1 AND 9),
    elevation_meters INT,
    image_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_favorite_spots (
    user_id BIGINT NOT NULL,
    spot_id BIGINT NOT NULL REFERENCES dark_sky_spots(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, spot_id)
);

-- Seed World-Class Dark Sky Locations
INSERT INTO dark_sky_spots (name, description, latitude, longitude, bortle_scale, elevation_meters, image_url)
VALUES
('Mauna Kea Observatory', 'Premier high-altitude Pacific observatory site with exceptionally pristine dark skies.', 19.8207, -155.4681, 1, 4207, 'https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86'),
('Aoraki Mackenzie Reserve', 'International Dark Sky Reserve in New Zealand showcasing panoramic Milky Way vistas.', -43.5950, 170.1418, 1, 760, 'https://images.unsplash.com/photo-1419242902214-272b3f66ee7a'),
('Atacama Desert Observatory', 'Located in northern Chile, recognized as one of the driest and darkest spots on Earth.', -23.8634, -69.1328, 1, 2400, 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23'),
('Hanle Dark Sky Reserve', 'High-altitude astronomical dark sky reserve located in Ladakh, India.', 32.7801, 78.9602, 2, 4500, 'https://images.unsplash.com/photo-1502134249126-9f3755a50d78')
ON CONFLICT DO NOTHING;