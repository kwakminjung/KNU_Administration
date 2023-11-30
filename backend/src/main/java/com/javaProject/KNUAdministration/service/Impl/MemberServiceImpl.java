package com.javaProject.KNUAdministration.service.Impl;

import com.javaProject.KNUAdministration.data.dto.MemberDto;
import com.javaProject.KNUAdministration.data.entity.Board;
import com.javaProject.KNUAdministration.data.entity.Member;
import com.javaProject.KNUAdministration.data.repository.BoardRepository;
import com.javaProject.KNUAdministration.data.repository.MemberRepository;
import com.javaProject.KNUAdministration.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    @Override
    @Transactional
    public MemberDto insertMember(MemberDto dto) {
        Member madeMember = MemberDto.resisterMemberEntity(dto);
        Member savedMember = memberRepository.save(madeMember);
        log.info("멤버 저장 완료");
        log.info(String.valueOf(madeMember));
        return MemberDto.createMemberDto(savedMember);
    }

    @Override
    public Boolean isUserExists(String accountId){
        Member member = memberRepository.findByAccountId(accountId);
        if(member == null){
            return false;
        }else{
            return true;
        }
    }
//    @Override
//    public Member selectMember(Long number) {
//        Optional<Member> selectMember = memberRepository.findById(number);
//        return selectMember.orElse(null);
//    }
//
//    @Override
//    public List<Member> allSelectMember() {
//        return memberRepository.findAll();
//    }

    @Override
    @Transactional
    public MemberDto updateMember(MemberDto memberDto) {
        log.info("전달받은 dto : " + memberDto.toString());
        Optional<Member> selectedMember = memberRepository.findById(memberDto.getMemberId());

        if (selectedMember.isPresent()) {
            Member tempMember = selectedMember.get();

            if (memberDto.getName() != null)
                tempMember.setName(memberDto.getName());
            if (memberDto.getPassword() != null)
                tempMember.setPassword(memberDto.getPassword());

            Member updatedMember = memberRepository.save(tempMember);
            return MemberDto.createMemberDto(updatedMember);
        } else {
            log.info("못찾음");
            throw new NoSuchElementException("해당 멤버를 찾을 수 없습니다.");
        }
    }

    @Override
    public MemberDto showMember(String accountId){
        Member member = memberRepository.findByAccountId(accountId);
        if(member!=null){
            return MemberDto.createMemberDto(member);
        }else{
            throw new NoSuchElementException("해당 멤버를 찾을 수 없습니다.");
        }
    }
    @Override
    @Transactional
    public MemberDto deleteMember(String accountId) {
        Member selectedMember = memberRepository.findByAccountId(accountId);

        List<Board> boards = boardRepository.findByAccountId(accountId);
        for(Board i : boards){
            i.setMember(null);
            i.setWriterId("(알 수 없음)");
        }
        if (selectedMember != null) {
            memberRepository.delete(selectedMember);
            log.info("삭제된 멤버 : " + selectedMember.toString());
            return MemberDto.createMemberDto(selectedMember);
        } else {
            throw new NoSuchElementException("해당 멤버를 찾을 수 없습니다.");
        }
    }
    @Override
    public int viewBoardNum(String accountId){
        Member member = memberRepository.findByAccountId(accountId);
        return member.getBoardNum();
    }
    @Override
    public String userLogin(String accountId) {
        Member member = memberRepository.findByAccountId(accountId);

        if (member == null) {
            throw new NoSuchElementException("해당 계정을 찾을 수 없습니다.");
        }

        return member.getPassword();

    }
}
