package com.javaProject.KNUAdministration.data.repository;

import com.javaProject.KNUAdministration.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository  extends JpaRepository<Member, Long> {
//    @Query("SELECT m FROM Member m WHERE m.accountId = :accountId")
    Member findByAccountId(String accountId);

}
