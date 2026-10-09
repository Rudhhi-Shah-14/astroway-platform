package com.astroway.catalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NasaApodDto implements Serializable {
    private String title;
    private String explanation;
    private String url;

    @JsonProperty("hdurl")
    private String hdUrl;

    @JsonProperty("media_type")
    private String mediaType;
    private String date;
}