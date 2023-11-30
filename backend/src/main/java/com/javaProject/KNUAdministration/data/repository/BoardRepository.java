package com.javaProject.KNUAdministration.data.repository;

import com.javaProject.KNUAdministration.data.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value="SELECT * "+
            "FROM Board "+
            "WHERE writer_id = :accountId",
            nativeQuery = true)//value 속성에 실행하려는 쿼리 작성
    List<Board> findByAccountId(String accountId);
    @Query("select p from Board p order by p.id desc")
    List<Board> findAllDesc();

    @Query("SELECT b FROM Board b WHERE b.state = 0")
    List<Board> findAllStateZero();
    List<Board> findByTitleContainingOrContentContaining(String title, String content);
    @Query("SELECT b FROM Board b ORDER BY b.view DESC")
    List<Board> findAllOrderByViewsDesc();
    List<Board> findByStateOrderByHasteNumDesc(int state);
    List<Board> findByWriterIdAndState(String writerId, int status);
    List<Board> findByCategoryAndState(String category, int status);
    List<Board> findByState(int state);
}
