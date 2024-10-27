package com.bankproject.bankproject.domain.board.enums;

public enum BoardType {

    FINANCE_NEWS("금융뉴스"),
    NOTICE("공지사항"),
    GUIDE("이용안내"),
    QNA("질문과 답변게시판");

    private String value;

    BoardType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
