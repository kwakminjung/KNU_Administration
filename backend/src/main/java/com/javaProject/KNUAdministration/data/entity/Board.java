package com.javaProject.KNUAdministration.data.entity;

import lombok.*;

import javax.persistence.*;


@Entity //클래스 자체는 테이블과 일대일로 매칭되며, 해당 클래스의 인스턴스는 매핑되는 테이블에서 하나의 레코드를 의미함
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {
    @Id //테이블의 기본값 역할, 엔티티 클래스의 필드는 테이블의 칼럼과 매핑됨
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String writerId;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String content;

    @Column(name="processing_confirmation_status",nullable = false)
    private int state;

    @Column(nullable = false)
    private String isModified;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @Column(columnDefinition = "integer default 0",nullable = false)
    private int hasteNum;

    @Column(nullable = false)
    private String category;
}
