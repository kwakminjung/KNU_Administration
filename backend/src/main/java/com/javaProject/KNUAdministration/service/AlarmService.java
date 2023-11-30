package com.javaProject.KNUAdministration.service;

import com.javaProject.KNUAdministration.data.dto.AlarmResponseDto;

import java.util.List;

public interface AlarmService {
    List<AlarmResponseDto> selectAlarm(Long memberId);
}
