package com.javaProject.KNUAdministration.data.repository;

import com.javaProject.KNUAdministration.data.entity.Alarm;
import com.javaProject.KNUAdministration.data.entity.Haste;
import com.javaProject.KNUAdministration.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query("select p from Alarm p where p.memberId = :memberId order by p.id desc")
    List<Alarm> findByMemberIdDesc(Long memberId);

}
