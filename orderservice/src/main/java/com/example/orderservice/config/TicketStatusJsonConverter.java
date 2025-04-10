package com.example.orderservice.config;

import com.example.orderservice.enums.TicketStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Converter(autoApply = false)
public class TicketStatusJsonConverter implements AttributeConverter<Map<LocalDateTime, TicketStatus>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<LocalDateTime, TicketStatus> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert TicketStatus map to JSON", e);
        }
    }

    @Override
    public Map<LocalDateTime, TicketStatus> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return Map.of();

        try {
            Map<String, String> tempMap = objectMapper.readValue(dbData, new TypeReference<>() {});
            return tempMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> LocalDateTime.parse(entry.getKey()),
                            entry -> TicketStatus.valueOf(entry.getValue())
                    ));
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to TicketStatus map", e);
        }
    }
}
