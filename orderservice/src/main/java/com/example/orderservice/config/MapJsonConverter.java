package com.example.orderservice.config;

import com.example.orderservice.enums.PackageStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;


@Converter(autoApply = true)
public class MapJsonConverter implements AttributeConverter<Map<LocalDateTime, PackageStatus>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<LocalDateTime, PackageStatus> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }

    @Override
    public Map<LocalDateTime, PackageStatus> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to map", e);
        }
    }
}