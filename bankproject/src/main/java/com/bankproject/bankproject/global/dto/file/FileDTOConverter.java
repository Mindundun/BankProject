package com.bankproject.bankproject.global.dto.file;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FileDtoConverter implements AttributeConverter<FileDtoWrapper, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(FileDtoWrapper attribute) {
        if (attribute == null || attribute.getFiles() == null || attribute.getFiles().isEmpty()) {
            return "[]"; // 빈 리스트를 저장할 때
        }

        try {
            return objectMapper.writeValueAsString(attribute.getFiles());
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패: " + e.getMessage());
        }
    }

    @Override
    public FileDtoWrapper convertToEntityAttribute(String dbData) {
        try {
            List<FileDto> files = objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, FileDto.class));
            FileDtoWrapper fileDTOWrapper = new FileDtoWrapper(files);
            return fileDTOWrapper;
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패: " + e.getMessage());
        }
    }
}
