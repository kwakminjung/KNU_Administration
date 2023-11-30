package com.example.knu_administration_android.alarm;

import static android.view.View.GONE;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.knu_administration_android.LocalDB.UserPreferences;
import com.example.knu_administration_android.R;
import com.example.knu_administration_android.dto.AlarmResponseDto;
import com.example.knu_administration_android.dto.MemberDto;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserAlarmFragment extends Fragment {

    private Context context;
    private ImageButton backBtn;
    private RecyclerView recyclerView;
    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_user_alarm, container, false);
        backBtn = root.findViewById(R.id.user_alarm_back_btn);
        recyclerView=root.findViewById(R.id.user_alarm_recyclerview);
        MemberDto savedMemberDto = UserPreferences.getUserInfo(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //뒤로 가기
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        //알림 띄우기
        userAlarmResponse(savedMemberDto.getMemberId());
        return root;
    }


    public void userAlarmResponse(Long memberId){
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.getAlarmState(memberId).enqueue(new Callback<List<AlarmResponseDto>>() {
            @Override
            public void onResponse(Call<List<AlarmResponseDto>> call, Response<List<AlarmResponseDto>> response) {
                if(response.isSuccessful()){
                    List<AlarmResponseDto> alarmList = response.body();
                    alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(alarmList);
                    recyclerView.setAdapter(alarmRecyclerViewAdapter);
                }
                else {
                    Log.e("retrofit", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AlarmResponseDto>> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });

    }
}