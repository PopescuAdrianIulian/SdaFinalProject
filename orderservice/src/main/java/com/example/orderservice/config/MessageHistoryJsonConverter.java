package com.example.orderservice.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Converter
public class MessageHistoryJsonConverter implements AttributeConverter<Map<LocalDateTime, String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<LocalDateTime, String> attribute) {
        try {
            // Convert Map to JSON string
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error while converting messageHistory to JSON", e);
        }
    }

    @Override
    public Map<LocalDateTime, String> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Map.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while converting JSON to messageHistory", e);
        }
    }
}
