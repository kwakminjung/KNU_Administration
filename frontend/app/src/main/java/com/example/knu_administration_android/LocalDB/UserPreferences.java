package com.example.knu_administration_android.LocalDB;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.knu_administration_android.dto.MemberDto;

public class UserPreferences {

    private static final String PREFS_NAME = "UserPreferences";
    private static final String KEY_MEMBER_ID = "member_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PASSWORD = "user_password";
    private static final String KEY_BOARD_NUM="board_num";

    // 사용자 정보 저장
    public static void saveUserInfo(Context context, MemberDto memberDto) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_MEMBER_ID, String.valueOf(memberDto.getMemberId()));
        editor.putString(KEY_USER_ID, memberDto.getAccountId());
        editor.putString(KEY_USER_PASSWORD, memberDto.getPassword());
        editor.putString(KEY_USER_NAME, memberDto.getName());
        editor.putString(KEY_BOARD_NUM, String.valueOf(memberDto.getBoardNum()));
        editor.apply();
    }

    // 사용자 정보 가져오기
    public static MemberDto getUserInfo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Long memberId = Long.parseLong(prefs.getString(KEY_MEMBER_ID, null));
        String userId = prefs.getString(KEY_USER_ID, null);
        String password = prefs.getString(KEY_USER_PASSWORD, null);
        String userName = prefs.getString(KEY_USER_NAME, null);
        int boardNum = Integer.parseInt(prefs.getString(KEY_BOARD_NUM, null));
        return new MemberDto(memberId, userId, password, userName, boardNum);
    }
}