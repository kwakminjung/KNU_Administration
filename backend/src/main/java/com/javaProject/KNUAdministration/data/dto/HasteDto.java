package com.javaProject.KNUAdministration.data.dto;

import com.javaProject.KNUAdministration.data.entity.Haste;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HasteDto {
    private Long memberId;
    private Long boardId;

}
