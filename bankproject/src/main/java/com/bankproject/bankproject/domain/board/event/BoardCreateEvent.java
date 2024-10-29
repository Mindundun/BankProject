package com.bankproject.bankproject.domain.board.event;

import org.springframework.context.ApplicationEvent;

import com.bankproject.bankproject.domain.board.entity.Board;

import lombok.Getter;

@Getter
public class BoardCreateEvent extends ApplicationEvent {

    private final Board board;

    public BoardCreateEvent(Object source, Board board) {
        super(source);
        this.board = board;
    }

}
