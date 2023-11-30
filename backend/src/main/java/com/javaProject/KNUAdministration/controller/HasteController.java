package com.javaProject.KNUAdministration.controller;

import com.javaProject.KNUAdministration.data.dto.HasteDto;
import com.javaProject.KNUAdministration.data.entity.Haste;
import com.javaProject.KNUAdministration.service.HasteService;
import com.javaProject.KNUAdministration.service.Impl.HasteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RestControllerAdvice
@RequiredArgsConstructor
public class HasteController {
    private final HasteServiceImpl hasteService;

    //급해요 정보 조회
    @GetMapping("/api/haste/{memberId}/{boardId}")
    public ResponseEntity<Boolean> showHaste(@PathVariable Long memberId,
                                             @PathVariable Long boardId){
        Boolean flag = hasteService.checkHaste(memberId, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(flag);
    }
    //급해요 삽입
    @PostMapping("/api/haste")
    public ResponseEntity<?> insertHaste(@RequestBody HasteDto hasteDto){
        int hasteNum = hasteService.insert(hasteDto);
        return ResponseEntity.status(HttpStatus.OK).body(hasteNum);
    }
    //급해요 삭제
    @DeleteMapping("/api/haste")
    public ResponseEntity<?> removeHaste(@RequestBody HasteDto hasteDto){
        int hasteNum = hasteService.remove(hasteDto);
        return ResponseEntity.status(HttpStatus.OK).body(hasteNum);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("실행 중 error가 발생했습니다");
    }
}
