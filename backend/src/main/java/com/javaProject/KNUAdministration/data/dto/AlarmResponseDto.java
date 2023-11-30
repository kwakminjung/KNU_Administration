package com.javaProject.KNUAdministration.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmResponseDto {
    private Long boardId;
    private String noticeTitle;
}
