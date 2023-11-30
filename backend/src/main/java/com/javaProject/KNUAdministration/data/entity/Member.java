package com.javaProject.KNUAdministration.data.entity;

import com.javaProject.KNUAdministration.data.dto.BoardDto;
import com.javaProject.KNUAdministration.data.dto.MemberDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity //클래스 자체는 테이블과 일대일로 매칭되며, 해당 클래스의 인스턴스는 매핑되는 테이블에서 하나의 레코드를 의미함
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    @Id //테이블의 기본값 역할, 엔티티 클래스의 필드는 테이블의 칼럼과 매핑됨
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int boardNum;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Haste> hastes = new ArrayList<>();


//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Board> boardList = new ArrayList<>();

    public static Member MemberDtoToEntity(MemberDto dto){
        Member member = new Member(
                dto.getMemberId(),
                dto.getAccountId(),
                dto.getPassword(),
                dto.getName(),
                dto.getBoardNum(),
                new ArrayList<>()
        );

        return member;
    }
    public void addHaste(Haste haste) {
        this.hastes.add(haste);
        haste.setMember(this);
    }

    public void removeHaste(Haste haste) {
        this.hastes.remove(haste);
        haste.setMember(null);
    }



}