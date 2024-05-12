package com.moisescp.Fipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VehicleData(
        @JsonAlias("Valor") String valuation,
        @JsonAlias("Marca") String brand,
        @JsonAlias("Modelo") String model,
        @JsonAlias("AnoModelo") Integer year,
        @JsonAlias("Combustivel") String fuelType
) {}
