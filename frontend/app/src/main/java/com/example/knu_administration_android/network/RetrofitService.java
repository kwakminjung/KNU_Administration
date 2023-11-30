package com.example.knu_administration_android.network;

import com.example.knu_administration_android.dto.AlarmResponseDto;
import com.example.knu_administration_android.dto.BoardDto;
import com.example.knu_administration_android.dto.BoardManagerUpdateDto;
import com.example.knu_administration_android.dto.HasteDto;
import com.example.knu_administration_android.dto.MemberDto;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitService {

    //로그인 시도 시 비밀번호 반환
    @GET("/api/userLogin/{accountId}")
    Call<String> userLogin(@Path("accountId") String accountId);

    //비밀번호 일치 시 MemberDto 반환
    @GET("/api/member/{accountId}")
    Call<MemberDto> getMember(@Path("accountId") String accountId);

    //회원가입 시 아이디 중복확인
    @GET("/api/userIsExist/{accountId}")
    Call<Boolean> checkUserIdExist(@Path("accountId") String accountId);

    //회원가입
    @POST("/api/member")
    Call<MemberDto> signUp(@Body MemberDto memberDto);

    //게시판 단일 조회
    @GET("/api/board/{boardId}")
    Call<BoardDto> showBoard(@Path("boardId") Long boardId);

    //최신 순으로 게시판 불러오기
    @GET("/api/BoardList/Latest")
    Call<List<BoardDto>> getLatestBoardList();

    //오래된 순으로 게시판 불러오기
    @GET("/api/BoardList/Old")
    Call<List<BoardDto>> getOldBoardList();

    //급해요 순으로 게시판 불러오기
    @GET("/api/BoardList/haste")
    Call<List<BoardDto>> showHasteBoard();

    //조회수 순으로 게시판 불러오기
    @GET("/api/BoardList/view")
    Call<List<BoardDto>> showViewBoard();

    //게시판 글 작성하기
    @POST("/api/board")
    Call<BoardDto> createBoard(@Body BoardDto boardDto);

    //급해요 눌렀는 지 아닌지 확인하기
    @GET("/api/haste/{memberId}/{boardId}")
    Call<Boolean> showHaste(@Path("memberId") Long memberId, @Path("boardId") Long boardId);

    //급해요 추가
    @POST("/api/haste")
    Call<Integer> insertHaste(@Body HasteDto hasteDto);

    //급해요 삭제
    @HTTP(method = "DELETE", path="/api/haste", hasBody = true)
    Call<Integer> removeHaste(@Body HasteDto hasteDto);

    //게시판 수정
    @PUT("/api/board")
    Call<BoardDto> updateBoard(@Body BoardDto boardDto);

    //게시판 삭제
    @HTTP(method = "DELETE", path="/api/board/{boardId}", hasBody = true)
    Call<BoardDto> deleteBoard(@Path("boardId") Long boardId);

    //관리자 게시판 수정(상태, 카테고리)
    @PATCH("/api/Board/{boardId}/manager")
    Call<BoardDto> updateManagement(
            @Path("boardId") Long boardId,
            @Body BoardManagerUpdateDto boardManagerUpdateDto
    );

    //검색한 게시판 가져오기
    @GET("/api/board/search/{keyword}")
    Call<List<BoardDto>> searchBoard(@Path("keyword") String keyword);

    //사용자 정보 수정
    @PUT("/api/member")
    Call<MemberDto> updateMember(@Body MemberDto memberDto);

    //사용자가 작성한 게시판 불러오기
    @GET("/api/BoardList/{accountId}")
    Call<List<BoardDto>> showMemberBoard(@Path("accountId") String accountId);

    //사용자가 급해요한 글 불러오기
    @GET("/api/BoardList/{accountId}/haste")
    Call<List<BoardDto>> showMemberHasteBoard(@Path("accountId") String accountId);

    //사용자 게시글 진행상태별 민원개수 조회
    @GET("/api/{accountId}/stateNum")
    Call<List<Integer>> showMemberState(@Path("accountId") String accountId);

    //관리자 전체 게시글 진행상태별 민원개수 조회
    @GET("/api/state")
    Call<List<Integer>>showState();

    //관리자 전체 게시글 카테고리&진행상태별 민원개수
    @GET("/api/stateNum/{category}")
    Call<List<Integer>> showStateAndCategoryBoardList(@Path("category") String category);

    //회원탈톼
    @HTTP(method = "DELETE", path="/api/member/{accountId}", hasBody = true)
    Call<MemberDto> deleteMember(@Path("accountId") String accountId);

    //사용자 알림
    @GET("/api/{memberId}/alarm/state")
    Call<List<AlarmResponseDto>> getAlarmState(@Path("memberId") Long memberId);

}