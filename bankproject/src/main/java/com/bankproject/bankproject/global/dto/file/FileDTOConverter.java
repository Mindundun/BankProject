package com.bankproject.bankproject.global.dto.file;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FileDTOConverter implements AttributeConverter<List<FileDTO>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<FileDTO> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]"; // 빈 리스트를 저장할 때
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패: " + e.getMessage());
        }
    }

    @Override
    public List<FileDTO> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, FileDTO.class));
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패: " + e.getMessage());
        }
    }
}
