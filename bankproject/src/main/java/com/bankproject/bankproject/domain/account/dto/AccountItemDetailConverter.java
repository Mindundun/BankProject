package com.bankproject.bankproject.domain.account.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

@Convert
public class AccountItemDetailConverter implements AttributeConverter<AccountItemDetail, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AccountItemDetail attribute) {
        if (attribute == null) {
            return "{}";
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패: " + e.getMessage());
        }
    }

    @Override
    public AccountItemDetail convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, AccountItemDetail.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패: " + e.getMessage());
        }
    }

    
}
