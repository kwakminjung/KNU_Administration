package com.javaProject.KNUAdministration.data.repository;

import com.javaProject.KNUAdministration.data.entity.Board;
import com.javaProject.KNUAdministration.data.entity.Haste;
import com.javaProject.KNUAdministration.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HasteRepository extends JpaRepository<Haste, Long> {
    Optional<Haste> findByMemberAndBoard(Member member, Board board);
}
