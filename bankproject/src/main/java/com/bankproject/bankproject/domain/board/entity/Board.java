package com.bankproject.bankproject.domain.board.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bankproject.bankproject.domain.board.enums.BoardType;

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
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    private String author;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

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
