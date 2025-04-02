package com.example.orderservice.config;

import com.example.orderservice.enums.PackageStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

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
            Map<String, String> tempMap = objectMapper.readValue(dbData, new TypeReference<Map<String, String>>() {});
            return tempMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> LocalDateTime.parse(entry.getKey()),
                            entry -> PackageStatus.valueOf(entry.getValue())
                    ));
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to map", e);
        }
    }
}
