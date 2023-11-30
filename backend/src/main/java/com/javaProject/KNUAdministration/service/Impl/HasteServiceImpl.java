package com.javaProject.KNUAdministration.service.Impl;

import com.javaProject.KNUAdministration.data.dto.HasteDto;
import com.javaProject.KNUAdministration.data.entity.Board;
import com.javaProject.KNUAdministration.data.entity.Haste;
import com.javaProject.KNUAdministration.data.entity.Member;
import com.javaProject.KNUAdministration.data.repository.BoardRepository;
import com.javaProject.KNUAdministration.data.repository.HasteRepository;
import com.javaProject.KNUAdministration.data.repository.MemberRepository;
import com.javaProject.KNUAdministration.service.HasteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HasteServiceImpl implements HasteService {
    private final HasteRepository hasteRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Override
    public Boolean checkHaste(Long memberId, Long boardId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Could not found member id : " + memberId));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("Could not found board id : " + boardId));

        Optional<Haste> haste = hasteRepository.findByMemberAndBoard(member, board);

        if(haste.isPresent())
            return true;
        else
            return false;

    }
    @Transactional
    @Override
    public int insert(HasteDto hasteDto) {
        Member member = memberRepository.findById(hasteDto.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("Could not found member id : " + hasteDto.getMemberId()));

        Board board = boardRepository.findById(hasteDto.getBoardId())
                .orElseThrow(() -> new NoSuchElementException("Could not found board id : " + hasteDto.getBoardId()));

        Optional<Haste> haste = hasteRepository.findByMemberAndBoard(member, board);

        if(haste.isPresent()){
            throw new NoSuchElementException("이미 급해요를 누른 회원");
        }
        int hasteNum = board.getHasteNum()+1;
        board.setHasteNum(hasteNum);
        boardRepository.save(board);

        Haste insetHaste = Haste.builder()
                .board(board)
                .member(member)
                .build();

        member.addHaste(insetHaste);
        memberRepository.save(member);
        return board.getHasteNum();
    }

    @Transactional
    @Override
    public int remove(HasteDto hasteDto) {
        Member member = memberRepository.findById(hasteDto.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("Could not found member id : " + hasteDto.getMemberId()));

        Board board = boardRepository.findById(hasteDto.getBoardId())
                .orElseThrow(() -> new NoSuchElementException("Could not found board id : " + hasteDto.getBoardId()));

        Haste haste = hasteRepository.findByMemberAndBoard(member, board)
                .orElseThrow(() -> new NoSuchElementException("Could not found haste Id"));

        int hasteNum = board.getHasteNum()-1;
        board.setHasteNum(hasteNum);
        boardRepository.save(board);

        member.removeHaste(haste);
        memberRepository.save(member);
        hasteRepository.delete(haste);

        return board.getHasteNum();
    }

}
