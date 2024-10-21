package com.bankproject.bankproject.domain.board.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bankproject.bankproject.domain.board.enums.BoardType;
import com.bankproject.bankproject.global.dto.FileDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "bank_board")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_type")
    @Enumerated(EnumType.STRING)
    private BoardType type;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "JSON")
    private String files;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "read_count")
    private Integer readCount;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        if(this.readCount == null) {
            this.readCount = 0;
        }

        if(this.isDeleted == null) {
            this.isDeleted = false;
        }
    }

    public void increaseReadCount() {
        this.readCount++;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void setFiles(List<FileDTO> fileDTOList) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            this.files = objectMapper.writeValueAsString(fileDTOList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("파일 정보를 JSON으로 변환하는데 실패했습니다.");
        }
    }

    public List<FileDTO> getFiles() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(this.files, objectMapper.getTypeFactory().constructCollectionType(List.class, FileDTO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("파일 정보를 객체로 변환하는데 실패했습니다.");
        }
    }

}
