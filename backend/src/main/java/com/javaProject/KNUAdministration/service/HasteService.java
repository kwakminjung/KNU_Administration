package com.javaProject.KNUAdministration.service;

import com.javaProject.KNUAdministration.data.dto.HasteDto;

public interface HasteService {
    Boolean checkHaste(Long memberId, Long boardId);
    int insert(HasteDto hasteDto);
    int remove(HasteDto hasteDto);
}
