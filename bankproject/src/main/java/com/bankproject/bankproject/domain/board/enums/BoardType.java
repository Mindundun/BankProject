package com.bankproject.bankproject.domain.board.enums;

public enum BoardType {

    notice("공지사항"),
    guide("이용안내"),
    qna("질문과 답변게시판");

    private String value;

    BoardType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
