package com.javaProject.KNUAdministration.service;

import com.javaProject.KNUAdministration.data.dto.BoardDto;
import com.javaProject.KNUAdministration.data.dto.BoardManagerUpdateDto;

import java.util.List;

public interface BoardService {
    BoardDto insertBoard(BoardDto boardDto);
    BoardDto selectBoard(Long boardId);
    List<BoardDto> allSelectMemberBoard(String accountId);
    List<BoardDto> allSelectMemberHasteBoard(String accountId);
    List<BoardDto> allSelectHasteBoard();
    List<BoardDto> allSelectViewBoard();
    List<BoardDto> allSelectBoard();
    List<BoardDto> searchBoard(String keyword);
    BoardDto updateBoard(BoardDto boardDto);
    BoardDto deleteBoard(Long number);
    List<BoardDto> allSelectOldBoard();
    BoardDto updateBoardManager(Long boardId, BoardManagerUpdateDto boardManagerUpdateDto);
    List<Integer> MemberStateList(String accountId);
    List<Integer> StateList();
    List<BoardDto> MemberStateBoardList(String accountId, int state);
    List<BoardDto> AllSelectStateList(int state);
    List<Integer> categorySelectStateList(String category);
}
