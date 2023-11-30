package com.javaProject.KNUAdministration.data.dto;

import com.javaProject.KNUAdministration.data.entity.Board;
import com.javaProject.KNUAdministration.data.entity.Member;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
    private Long boardId;
    private String title;
    private String writerId;
    private String date;
    private String content;
    private int state; //처리과정
    private String isModified;
    private int view;
    private int hasteNum;
    private String category;

    public static BoardDto createBoardDto(Board board) {
        return new BoardDto(
                board.getBoardId(),
                board.getTitle(),
                board.getWriterId(),
                board.getDate(),
                board.getContent(),
                board.getState(),
                board.getIsModified(),
                board.getView(),
                board.getHasteNum(),
                board.getCategory()
        );
    }
    public static Board CreateBoardDtoToEntity(BoardDto boardDto, Member member){
        return new Board(
                null,
                boardDto.getTitle(),
                member.getAccountId(),
                null,
                boardDto.getContent(),
                boardDto.getState(),
                "",
                member,
                boardDto.getView(),
                boardDto.getHasteNum(),
                boardDto.getCategory()
        );
    }
    public static Board BoardDtoToEntity(BoardDto boardDto, Member member){
        return new Board(
                boardDto.getBoardId(),
                boardDto.getTitle(),
                member.getAccountId(),
                boardDto.getDate(),
                boardDto.getContent(),
                boardDto.getState(),
                boardDto.getIsModified(),
                member,
                boardDto.getView(),
                boardDto.getHasteNum(),
                boardDto.getCategory()
        );
    }
}
