package com.javaProject.KNUAdministration.controller;

import com.javaProject.KNUAdministration.data.dto.AlarmResponseDto;
import com.javaProject.KNUAdministration.data.dto.HasteDto;
import com.javaProject.KNUAdministration.service.Impl.AlarmServiceImpl;
import lombok.Getter;
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
public class AlarmController {
    private final AlarmServiceImpl alarmService;
    @GetMapping("/api/{memberId}/alarm/state")
    public ResponseEntity<List<AlarmResponseDto>> insertHaste(@PathVariable Long memberId){
        List<AlarmResponseDto> alarmResponseDtoList = alarmService.selectAlarm(memberId);
        return (alarmResponseDtoList != null)?
                ResponseEntity.status(HttpStatus.OK).body(alarmResponseDtoList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
