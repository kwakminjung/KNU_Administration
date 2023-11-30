    package com.javaProject.KNUAdministration.service.Impl;

    import com.javaProject.KNUAdministration.data.dto.BoardDto;
    import com.javaProject.KNUAdministration.data.dto.BoardManagerUpdateDto;
    import com.javaProject.KNUAdministration.data.entity.Alarm;
    import com.javaProject.KNUAdministration.data.entity.Board;
    import com.javaProject.KNUAdministration.data.entity.Haste;
    import com.javaProject.KNUAdministration.data.entity.Member;
    import com.javaProject.KNUAdministration.data.repository.AlarmRepository;
    import com.javaProject.KNUAdministration.data.repository.BoardRepository;
    import com.javaProject.KNUAdministration.data.repository.MemberRepository;
    import com.javaProject.KNUAdministration.service.BoardService;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.Duration;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.*;
    import java.util.stream.Collectors;

    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class BoardServiceImpl implements BoardService {
        private final BoardRepository boardRepository;
        private final MemberRepository memberRepository;
        private final AlarmRepository alarmRepository;

        @Override
        @Transactional
        public BoardDto insertBoard(BoardDto boardDto){
            log.info(boardDto.toString());
            boardDto.setBoardId(null);
            Member member = memberRepository.findByAccountId(boardDto.getWriterId());
            log.info("insertBoard/멤버 타입 변경 성공 >> "+member);
            Board board = BoardDto.CreateBoardDtoToEntity(boardDto, member);

            log.info("insertBoard/board 객체 생성 >> "+board);
            LocalDateTime now = LocalDateTime.now();
            String Date = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"));
            board.setDate(Date);
            Board savedBoard = boardRepository.save(board);
            log.info("insertBoard/저장 완료");

            if (savedBoard == null) {
                throw new NoSuchElementException("게시물을 저장할 수 없습니다.");
            }
            int memberTmp = member.getBoardNum()+1;
            member.setBoardNum(memberTmp);
            memberRepository.save(member);
            return BoardDto.createBoardDto(savedBoard);
        }

        @Override
        public BoardDto selectBoard(Long boardId){
            Optional<Board> selectBoard = boardRepository.findById(boardId);

            if (selectBoard.isPresent()) {
                Board board = selectBoard.get();
                board.setView(board.getView()+1); //조회수 증가
                boardRepository.save(board);

                BoardDto boardDto = BoardDto.createBoardDto(board);
                boardDto.setDate(TimeRefactoring(board.getDate()));
                return boardDto;
            } else {
                throw new NoSuchElementException("게시물을 찾을 수 없습니다.");
            }
        }

        private String TimeRefactoring(String time){
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(time, formatter);

            Duration duration = Duration.between(date, now);

            String tmp = "";
            if(duration.toDays() < 365){
                tmp = date.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
            }else{
                tmp += date.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
            }
            return tmp;
        }
        private String TimeRefactoringList(String time){
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(time, formatter);

            Duration duration = Duration.between(date, now);

            String tmp = "";
            if(duration.toHours() < 1){
                long minutes = duration.toMinutesPart();
                tmp += Long.toString(minutes) + "분 전";
            }else if(duration.toHours() < 24){
                tmp = date.format(DateTimeFormatter.ofPattern("HH:mm"));
            }else if(duration.toDays() < 365){
                tmp = date.format(DateTimeFormatter.ofPattern("MM/dd"));
            }else{
                long years = duration.toDays() / 365;
                tmp += Long.toString(years) + "년 전";
            }
            return tmp;
        }
        @Override
        public List<BoardDto> allSelectMemberBoard(String accountId){
            List<BoardDto> boardList = boardRepository.findByAccountId(accountId)
                    .stream()
                    .map(BoardDto::createBoardDto)
                    .collect(Collectors.toList());
            for(BoardDto i: boardList){
                String tmp = TimeRefactoringList(i.getDate());
                i.setDate(tmp);
            }
            return boardList;
        }
        @Override
        public List<BoardDto> allSelectMemberHasteBoard(String accountId){
            Member member = memberRepository.findByAccountId(accountId);
            List<Haste> hastes = member.getHastes();
            List<Board> boardList = new ArrayList<>();
            for(Haste haste : hastes){
                boardList.add(haste.getBoard());
            }
            List<BoardDto> boardResponseList = boardList
                    .stream()
                    .map(BoardDto::createBoardDto)
                    .collect(Collectors.toList());
            for(BoardDto i: boardResponseList){
                String tmp = TimeRefactoringList(i.getDate());
                i.setDate(tmp);
            }
            return boardResponseList;
        }
        @Override
        public List<BoardDto> allSelectHasteBoard(){
            List<BoardDto> boardList = boardRepository.findByStateOrderByHasteNumDesc(0)
                    .stream()
                    .map(BoardDto::createBoardDto)
                    .collect(Collectors.toList());
            for(BoardDto i: boardList){
                String tmp = TimeRefactoringList(i.getDate());
                i.setDate(tmp);
            }
            return boardList;
        }
        public List<BoardDto> allSelectViewBoard(){
            List<BoardDto> boardList = boardRepository.findAllOrderByViewsDesc()
                    .stream()
                    .map(BoardDto::createBoardDto)
                    .collect(Collectors.toList());
            for(BoardDto i: boardList){
                String tmp = TimeRefactoringList(i.getDate());
                i.setDate(tmp);
            }
            return boardList;
        }
        @Override
        public List<BoardDto> allSelectOldBoard() {
            List<BoardDto> boardList = boardRepository.findAllStateZero()
                    .stream()
                    .map(BoardDto::createBoardDto)
                    .collect(Collectors.toList());
            return boardList;
        }

        @Override
        public List<BoardDto> allSelectBoard() {
            List<BoardDto> boardList = boardRepository.findAllDesc()
                    .stream()
                    .map(BoardDto::createBoardDto)
                    .collect(Collectors.toList());
            for(BoardDto i: boardList){
                String tmp = TimeRefactoringList(i.getDate());
                i.setDate(tmp);
            }
            return boardList;
        }
        @Override
        public List<Integer> MemberStateList(String accountId){
            List<Integer> stateList = new ArrayList<>();
            for(int i = 0 ; i < 4; i++){
                List<Board> boardList = boardRepository.findByWriterIdAndState(accountId, i);
                stateList.add(boardList.size());
            }
            return stateList;
        }
        @Override
        public List<BoardDto> MemberStateBoardList(String accountId, int state){
            List<BoardDto> boardList = boardRepository.findByWriterIdAndState(accountId, state)
                    .stream()
                    .map(BoardDto::createBoardDto)
                    .collect(Collectors.toList());
            for(BoardDto i: boardList){
                String tmp = TimeRefactoringList(i.getDate());
                i.setDate(tmp);
            }
            return boardList;
        }
        @Override
        public List<Integer> StateList(){
            List<Integer> stateList = new ArrayList<>();
            for(int i = 0 ; i < 4; i++){
                List<Board> boardList = boardRepository.findByState(i);
                stateList.add(boardList.size());
            }
            return stateList;
        }
        @Override
        public List<BoardDto> AllSelectStateList(int state){
            log.info("BoardService >AllSelectStateList > state : "+state);
            List<Board> boards = boardRepository.findByState(state);

            if(boards != null){
                List<BoardDto> boardList = boards
                        .stream()
                        .map(BoardDto::createBoardDto)
                        .collect(Collectors.toList());
                for(BoardDto i: boardList){
                    String tmp = TimeRefactoringList(i.getDate());
                    i.setDate(tmp);
                }
                return boardList;
            }else{
                throw new NoSuchElementException("게시물을 찾을 수 없습니다.");
            }

        }
        @Override
        public List<Integer> categorySelectStateList(String category){
            List<Integer> stateList = new ArrayList<>();
            for(int i = 0 ; i < 4; i++){
                List<Board> boardList = boardRepository.findByCategoryAndState(category, i);
                stateList.add(boardList.size());
            }
            return stateList;
        }
        @Override
        public List<BoardDto> searchBoard(String keyword){
            List<BoardDto> boardDtoList = boardRepository.findByTitleContainingOrContentContaining(keyword, keyword)
                    .stream()
                    .map(BoardDto::createBoardDto)
                    .collect(Collectors.toList());
            for(BoardDto i: boardDtoList){
                String tmp = TimeRefactoringList(i.getDate());
                i.setDate(tmp);
            }
            return boardDtoList;
        }
        @Override
        @Transactional
        public BoardDto updateBoard(BoardDto boardDto) {
            Optional<Board> selectedBoard = boardRepository.findById(boardDto.getBoardId());
            log.info(selectedBoard.toString());

            if(!selectedBoard.get().getWriterId().equals(boardDto.getWriterId())){
                log.info(selectedBoard.get().getWriterId()+"  "+boardDto.getWriterId());
                throw new NoSuchElementException("작성자의 id가 일치하지 않습니다");
            }
            if (selectedBoard.isPresent()) {
                Board board = selectedBoard.get();

                board.setTitle(boardDto.getTitle());
                board.setContent(boardDto.getContent());
                board.setState(boardDto.getState());
                LocalDateTime now = LocalDateTime.now();
                String Date = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"));
                board.setDate(Date);
                board.setCategory(boardDto.getCategory());

                board.setIsModified("수정됨");
                return BoardDto.createBoardDto(board);
            } else {
                throw new NoSuchElementException("게시물을 찾을 수 없습니다.");
            }
        }

        @Override
        @Transactional
        public BoardDto updateBoardManager(Long boardId, BoardManagerUpdateDto boardManagerUpdateDto){
            Optional<Board> selectedBoard = boardRepository.findById(boardId);

            ArrayList<String> stateList = new ArrayList<>(Arrays.asList("접수 전", "접수 중", "처리 중", "처리 완료"));

            if(selectedBoard.isPresent()){
                Board board = selectedBoard.get();
                board.setState(boardManagerUpdateDto.getState());
                board.setCategory(boardManagerUpdateDto.getCategory());
                boardRepository.save(board);

                Member member = memberRepository.findByAccountId(board.getWriterId());

                int idx = boardManagerUpdateDto.getState();

//                Alarm insertAlarm = new Alarm();
//                insertAlarm.setBoard(board);
//                insertAlarm.setTitle("민원글의 진행상황이 "+stateList.get(idx)+"(으)로 변경되었습니다");
//                insertAlarm.setMemberId(member.getMemberId());
                Alarm insertAlarm = Alarm.builder()
                        .board(board)
                        .memberId(member.getMemberId())
                        .title("민원글의 진행상황이 "+stateList.get(idx)+"(으)로 변경되었습니다")
                        .build();
                alarmRepository.save(insertAlarm);

//                if(selectedBoard.get().getState() != boardManagerUpdateDto.getState()){
//                    int idx = boardManagerUpdateDto.getState();
//
    //                Alarm insetAlarm = Alarm.builder()
    //                        .board(board)
    //                        .member(member)
    //                        .title("민원글의 진행상황이 "+stateList.get(idx)+"(으)로 변경되었습니다")
    //                        .build();
//                      member.addAlarm(insetAlarm);
//                }
//                if(selectedBoard.get().getCategory() != boardManagerUpdateDto.getCategory()){
//                    Alarm insetAlarm = Alarm.builder()
//                            .board(board)
//                            .member(member)
//                            .title("민원글의 카테고리가 "+boardManagerUpdateDto.getCategory()+"(으)로 변경되었습니다")
//                            .build();
//                    member.addAlarm(insetAlarm);
//
//                }
//                memberRepository.save(member);
                return BoardDto.createBoardDto(board);
            }else {
                throw new NoSuchElementException("게시물을 찾을 수 없습니다.");
            }
        }

        @Override
        @Transactional
        public BoardDto deleteBoard(Long number){
            Optional<Board> selectedBoard = boardRepository.findById(number);

            if (selectedBoard.isPresent()) {
                Board board = selectedBoard.get();
                boardRepository.delete(board);
                return BoardDto.createBoardDto(board);
            } else {
                throw new NoSuchElementException("게시물을 찾을 수 없습니다.");
            }
        }

    }
