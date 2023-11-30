package com.javaProject.KNUAdministration.controller;

import com.javaProject.KNUAdministration.data.dto.BoardDto;
import com.javaProject.KNUAdministration.data.dto.BoardManagerUpdateDto;
import com.javaProject.KNUAdministration.service.Impl.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RestControllerAdvice
public class BoardController {
    private final BoardServiceImpl boardService;
    //보드 단일조회
    @GetMapping("/api/board/{boardId}")
    public ResponseEntity<BoardDto> showBoard(@PathVariable Long boardId){
        BoardDto boardDto = boardService.selectBoard(boardId);
        return (boardDto != null)?
                ResponseEntity.status(HttpStatus.OK).body(boardDto):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //보드 전체조회 최신순
    @GetMapping("/api/BoardList/Latest")
    public ResponseEntity<List<BoardDto>> showBoard(){
        List<BoardDto> boardDtoList = boardService.allSelectBoard();
        return (boardDtoList != null)?
                ResponseEntity.status(HttpStatus.OK).body(boardDtoList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //보드 전체조회 오래된 순
    @GetMapping("/api/BoardList/Old")
    public ResponseEntity<List<BoardDto>> showOldBoard(){
        List<BoardDto> boardDtoList = boardService.allSelectOldBoard();
        return (boardDtoList != null)?
                ResponseEntity.status(HttpStatus.OK).body(boardDtoList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //사용자가 쓴 글 조회
    @GetMapping("/api/BoardList/{accountId}")
    public ResponseEntity<List<BoardDto>> showMemberBoard(@PathVariable String accountId){
        List<BoardDto> boardDtoList = boardService.allSelectMemberBoard(accountId);
        return (boardDtoList != null)?
                ResponseEntity.status(HttpStatus.OK).body(boardDtoList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //사용자가 급해요한 글 전체조회
    @GetMapping("/api/BoardList/{accountId}/haste")
    public ResponseEntity<List<BoardDto>> showMemberHasteBoard(@PathVariable String accountId){
        List<BoardDto> boardDtoList = boardService.allSelectMemberHasteBoard(accountId);
        return (boardDtoList != null)?
                ResponseEntity.status(HttpStatus.OK).body(boardDtoList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //급해요순 전체조회
    @GetMapping("/api/BoardList/haste")
    public ResponseEntity<List<BoardDto>> showHasteBoard(){
        List<BoardDto> BoardDtoList =boardService.allSelectHasteBoard();
        return (BoardDtoList != null)?
                ResponseEntity.status(HttpStatus.OK).body(BoardDtoList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //조회수순 전체조회
    @GetMapping("/api/BoardList/view")
    public ResponseEntity<List<BoardDto>> showViewBoard(){
        List<BoardDto> BoardDtoList = boardService.allSelectViewBoard();
        return (BoardDtoList != null)?
                ResponseEntity.status(HttpStatus.OK).body(BoardDtoList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //사용자의 진행상태별 민원개수
    @GetMapping("/api/{accountId}/stateNum")
    public ResponseEntity<List<Integer>> showMemberState(@PathVariable String accountId){
        List<Integer> list = boardService.MemberStateList(accountId);
        return (list != null)?
                ResponseEntity.status(HttpStatus.OK).body(list):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //사용자의 진행상태별 민원 전체조회
    @GetMapping("/api/BoardList/state/{accountId}/{state}")
    public ResponseEntity<List<BoardDto>> showMemberState(@PathVariable String accountId,
                                                                  @PathVariable int state){
        List<BoardDto> list = boardService.MemberStateBoardList(accountId, state);
        return (list != null)?
                ResponseEntity.status(HttpStatus.OK).body(list):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //state별 전체 민원수 조회
    @GetMapping("/api/state")
    public ResponseEntity<List<Integer>> showState(){
        List<Integer> list = boardService.StateList();
        return (list != null)?
                ResponseEntity.status(HttpStatus.OK).body(list):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //state별 민원글 전체조회
    @GetMapping("/api/BoardList/state/{state}")
    public ResponseEntity<List<BoardDto>> showStateBoardList(@PathVariable int state){
        log.info("BoardController : showStateBoardList");
        List<BoardDto> list = boardService.AllSelectStateList(state);
        return (list != null)?
                ResponseEntity.status(HttpStatus.OK).body(list):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //카테고리별 state 개수
    @GetMapping("/api/stateNum/{category}")
    public ResponseEntity<List<Integer>> showStateAndCategoryBoardList(@PathVariable String category){
        List<Integer> list = boardService.categorySelectStateList(category);
        return (list != null)?
                ResponseEntity.status(HttpStatus.OK).body(list):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //관리자가 민원글 변경
    @PatchMapping("/api/Board/{boardId}/manager")
    public ResponseEntity<BoardDto> updateManagement(@PathVariable Long boardId,
                                                @RequestBody BoardManagerUpdateDto boardManagerUpdateDto){
        BoardDto updated = boardService.updateBoardManager(boardId, boardManagerUpdateDto);
        return(updated != null)?
                ResponseEntity.status(HttpStatus.OK).body(updated):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    //민원글 등록
    @PostMapping("/api/board")
    public ResponseEntity<BoardDto> createBoard(@RequestBody BoardDto boardDto){
        BoardDto created = boardService.insertBoard(boardDto);
        return (created != null)?
                ResponseEntity.status(HttpStatus.OK).body(created):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //민원글 수정
    @PutMapping("/api/board")
    public ResponseEntity<BoardDto> updateBoard(@RequestBody BoardDto boardDto){
        BoardDto updated = boardService.updateBoard(boardDto);
        return (updated != null)?
                ResponseEntity.status(HttpStatus.OK).body(updated):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //민원글 삭제
    @DeleteMapping("/api/board/{boardId}")
    public ResponseEntity<BoardDto> deleteBoard(@PathVariable Long boardId){
        BoardDto deleted = boardService.deleteBoard(boardId);
        return (deleted != null)?
                ResponseEntity.status(HttpStatus.OK).body(deleted):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //검색 요청
    @GetMapping("/api/board/search/{keyword}")
    public ResponseEntity<List<BoardDto>> searchBoard(@PathVariable String keyword){
        List<BoardDto> BoardDtoList = boardService.searchBoard(keyword);
        return (BoardDtoList != null)?
                ResponseEntity.status(HttpStatus.OK).body(BoardDtoList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @ExceptionHandler(NoSuchFieldException.class)
    public ResponseEntity<String> handleNoSuchFieldException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("정보를 찾을 수 없습니다");
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("실행 중 error가 발생했습니다");
    }
//    @GetMapping("/api/board/search")
//    public List<Board> searchBoards(@RequestParam String keyword) {
//        // 검색어를 사용하여 게시물을 검색하고 결과를 반환하는 로직을 구현
//        List<Board> searchResults = boardService.searchBoards(keyword);
//        return searchResults;
//    }
}
