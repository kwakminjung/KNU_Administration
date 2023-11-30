package com.javaProject.KNUAdministration.service.Impl;

import com.javaProject.KNUAdministration.data.dto.AlarmResponseDto;
import com.javaProject.KNUAdministration.data.entity.Alarm;
import com.javaProject.KNUAdministration.data.entity.Member;
import com.javaProject.KNUAdministration.data.repository.AlarmRepository;
import com.javaProject.KNUAdministration.data.repository.MemberRepository;
import com.javaProject.KNUAdministration.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<AlarmResponseDto> selectAlarm(Long memberId){
        Optional<Alarm> alarm = alarmRepository.findById(memberId);
        log.info(alarm.get().toString());

        List<Alarm> alarms =  alarmRepository.findByMemberIdDesc(memberId);

        List<AlarmResponseDto> alarmResponseDtoList = new ArrayList<>();
        for(Alarm i : alarms){
            String boardTmpString = null;
            if(i.getBoard().getTitle().length() < 8){
                boardTmpString = i.getBoard().getTitle();
            }else{
                boardTmpString = i.getBoard().getTitle().substring(0,8);
                boardTmpString += "...";
            }

            AlarmResponseDto alarmResponseDto = new AlarmResponseDto(
                    i.getBoard().getBoardId(),
                    i.getTitle()+" : "+boardTmpString
            );
            alarmResponseDtoList.add(alarmResponseDto);
        }
        return alarmResponseDtoList;
    }
}
