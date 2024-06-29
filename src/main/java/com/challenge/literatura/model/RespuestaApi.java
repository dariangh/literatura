package com.challenge.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record RespuestaApi(
        @JsonAlias("count") int count,
        @JsonAlias("results") List<DatosLibro> results) {
}
