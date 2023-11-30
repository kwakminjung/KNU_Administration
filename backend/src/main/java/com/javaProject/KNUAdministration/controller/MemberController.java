package com.javaProject.KNUAdministration.controller;

import com.javaProject.KNUAdministration.data.dto.MemberDto;
import com.javaProject.KNUAdministration.service.Impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RestControllerAdvice
public class MemberController {
    private final MemberServiceImpl memberService;

    //http://localhost:8080/userLogin?accountId=accountId&password=password
    //로그인 시에 id 존재 여부 판별 및 id에 해당하는 password반환
    @GetMapping("/api/userLogin/{accountId}")
    public ResponseEntity<String> userLogin(@PathVariable String accountId){
        MemberDto responseDto;
        String password = memberService.userLogin(accountId);

        return (password != null)?
                ResponseEntity.status(HttpStatus.OK).body(password):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //로그인 시에 password 일치확인 후 memberDto요청
    @GetMapping("/api/member/{accountId}")
    public ResponseEntity<MemberDto> Login(@PathVariable String accountId){
        MemberDto responseDto = memberService.showMember(accountId);

        return (responseDto != null)?
                ResponseEntity.status(HttpStatus.OK).body(responseDto):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //id 중복확인 여부
    @GetMapping("/api/userIsExist/{accountId}")
    public ResponseEntity<Boolean> userIdExist(@PathVariable String accountId){
        MemberDto responseDto;
        Boolean isUserExist = memberService.isUserExists(accountId);

        return (isUserExist != null)?
                ResponseEntity.status(HttpStatus.OK).body(isUserExist):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //사용자가 작성한 민원글 전체 수 조회
    @GetMapping("/api/{accountId}/writing-board-num")
    public ResponseEntity<Integer> userBoardNum(@PathVariable String accountId){
        int num = memberService.viewBoardNum(accountId);

        return ResponseEntity.status(HttpStatus.OK).body(num);

    }
    //회원가입 요청
    @PostMapping("/api/member")
    public ResponseEntity<MemberDto> create(@RequestBody MemberDto dto){
        MemberDto created = memberService.insertMember(dto);
        return (created != null)?
                ResponseEntity.status(HttpStatus.OK).body(created):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //회원정보 수정
    @PutMapping("/api/member")
    public ResponseEntity<MemberDto> update(@RequestBody MemberDto dto){
        MemberDto updated = memberService.updateMember(dto);
        return(updated != null)?
                ResponseEntity.status(HttpStatus.OK).body(updated):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }
    //회원탈퇴
    @DeleteMapping("/api/member/{accountId}")
    public ResponseEntity<MemberDto> delete(@PathVariable String accountId){
        MemberDto deleted = memberService.deleteMember(accountId);
        log.info("delete : memberService에게 전달받은 객체 : " + deleted);
        return (deleted != null)?
                ResponseEntity.status(HttpStatus.OK).body(deleted):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(NoSuchFieldException.class)
    public ResponseEntity<String> handleNoSuchFieldException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("정보를 찾을 수 없습니다");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("실행 중 error가 발생했습니다");
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
