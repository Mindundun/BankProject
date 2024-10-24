package com.bankproject.bankproject.domain.board.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bankproject.bankproject.domain.board.enums.BoardType;
import com.bankproject.bankproject.global.dto.file.FileDTO;
import com.bankproject.bankproject.global.dto.file.FileDTOConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Convert(converter = FileDTOConverter.class)
    @Column(name = "files", columnDefinition = "JSON")
    private List<FileDTO> files;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updateBy;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updateDate;

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

}
