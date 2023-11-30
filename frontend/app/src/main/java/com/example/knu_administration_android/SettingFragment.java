package com.example.knu_administration_android;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.knu_administration_android.LocalDB.UserPreferences;
import com.example.knu_administration_android.alarm.UserAlarmFragment;
import com.example.knu_administration_android.dto.MemberDto;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;
import com.example.knu_administration_android.setting.MemberViewModel;
import com.example.knu_administration_android.setting.SettingEditProfileFragment;
import com.example.knu_administration_android.setting.SettingMyArticlesFragment;
import com.example.knu_administration_android.setting.SettingMyHasteFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {
    private Context context;
    private Button MyArticlesBtn;
    private TextView myPageTitle, settingName, editProfileBtn, settingID;
    private Button logoutBtn, withdrawBtn;
    private TextView settingBeforeCheck, settingAfterCheck, settingProcessing, settingProcessedComplete;
    private BarChart barChart;
    private TextView checkBox_elevator, checkBox_heating_and_cooling, checkBox_dangerous_goods,
            checkBox_laboratory, checkBox_classroom, checkBox_dormitory, checkBox_etc, checkBox_whole;
    private MemberDto savedMemberDto;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;
    private String chooseCategory;
    private LoginFragment loginFragment;
    private ImageButton userAlarm;
    private Button myHasteBtn;
    private MemberViewModel memberViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberViewModel = new ViewModelProvider(requireActivity()).get(MemberViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        memberViewModel.getMemberDto().observe(this, new Observer<MemberDto>() {
            @Override
            public void onChanged(MemberDto newMemberDto) {
                // 변경된 데이터로 UI 업데이트
                settingName.setText(newMemberDto.getName());
                settingID.setText(newMemberDto.getAccountId());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        context = container.getContext();
        settingName = root.findViewById(R.id.setting_name);
        settingID = root.findViewById(R.id.setting_ID);
        MyArticlesBtn = root.findViewById(R.id.my_articles_btn);
        savedMemberDto = UserPreferences.getUserInfo(getContext());
        settingName.setText(savedMemberDto.getName());
        settingID.setText(savedMemberDto.getAccountId());
        Log.e("onCreateView",savedMemberDto.getName());
        logoutBtn = root.findViewById(R.id.logoutBtn);
        withdrawBtn = root.findViewById(R.id.withdrawBtn);

        myPageTitle=root.findViewById(R.id.mypage_title);
        myHasteBtn =root.findViewById(R.id.my_haste_btn);
        settingBeforeCheck = root.findViewById(R.id.setting_before_check);
        settingAfterCheck = root.findViewById(R.id.setting_after_check);
        settingProcessing = root.findViewById(R.id.setting_processing);
        settingProcessedComplete = root.findViewById(R.id.setting_processed_complete);
        barChart = (BarChart) root.findViewById(R.id.chart);

        checkBox_elevator = root.findViewById(R.id.checkbox_elevator);
        checkBox_heating_and_cooling = root.findViewById(R.id.checkbox_heating_and_cooling);
        checkBox_dangerous_goods = root.findViewById(R.id.checkbox_dangerous_goods);
        checkBox_laboratory = root.findViewById(R.id.checkBox_laboratory);
        checkBox_classroom = root.findViewById(R.id.checkbox_classroom);
        checkBox_dormitory = root.findViewById(R.id.checkbox_dormitory);
        checkBox_etc = root.findViewById(R.id.checkbox_etc);
        checkBox_whole = root.findViewById(R.id.checkbox_whole);
        loginFragment=new LoginFragment();
        userAlarm = root.findViewById(R.id.user_alarm);

        //사용자 알림
        if(savedMemberDto.getAccountId().equals(getString(R.string.managerId))){
           userAlarm.setVisibility(View.GONE);
        }
        userAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, new UserAlarmFragment())
                        .addToBackStack("UserAlarmFragment")
                        .commit();
            }
        });

        //내 게시물
        MyArticlesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, new SettingMyArticlesFragment())
                        .addToBackStack("settingMyArticlesFragment")
                        .commit();
            }
        });

        myHasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, new SettingMyHasteFragment())
                        .addToBackStack("settingMyHasteFragment")
                        .commit();
            }
        });

        //프로필 수정
        editProfileBtn = root.findViewById(R.id.edit_profile_btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingEditProfileFragment settingEditProfileFragment = new SettingEditProfileFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, settingEditProfileFragment)
                        .addToBackStack("settingEditProfileFragment")
                        .commit();
            }
        });

        //로그아웃
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFragment.setLoggedIn(false);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, loginFragment)
                        .commit();


            }
        });


        //탈퇴
        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofitClient= RetrofitClient.getInstance();
                retrofitAPI=RetrofitClient.getRetrofitInterface();
                retrofitAPI.deleteMember(savedMemberDto.getAccountId()).enqueue(new Callback<MemberDto>() {
                    @Override
                    public void onResponse(Call<MemberDto> call, Response<MemberDto> response) {
                        if(response.isSuccessful()){

                            loginFragment.setLoggedIn(false);
                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frameLayout, loginFragment)
                                    .commit();
                            Toast.makeText(context,"탈퇴가 완료되었습니다",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.e("withdraw", String.valueOf(response.code()));
                            Toast.makeText(context,"다시 시도해주십시오",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<MemberDto> call, Throwable t) {
                        Log.e("retrofit", "Failed to connect: " + t.getMessage());
                    }
                });


            }
        });

        //관리자면 탈퇴, 내가 작성한 글 안보이고 그래프, 표띄우기
        if(savedMemberDto.getAccountId().equals(getString(R.string.managerId))){
            chooseCategory="전체";
            setCheckColor(7);
            managerStateTable();
            withdrawBtn.setVisibility(View.GONE);
            MyArticlesBtn.setVisibility(View.GONE);
            myHasteBtn.setVisibility(View.GONE);
            myPageTitle.setText("민원 처리 현황");

        }
        else{
            userStateTable();
            checkBox_elevator.setVisibility(View.GONE);
            checkBox_classroom.setVisibility(View.GONE);
            checkBox_dormitory.setVisibility(View.GONE);
            checkBox_dangerous_goods.setVisibility(View.GONE);
            checkBox_heating_and_cooling.setVisibility(View.GONE);
            checkBox_laboratory.setVisibility(View.GONE);
            checkBox_etc.setVisibility(View.GONE);
            checkBox_whole.setVisibility(View.GONE);
            barChart.setVisibility(View.GONE);
        }


        //그래프 버튼
        checkBox_elevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckColor(0);
                chooseCategory="승강기";
                managerCategoryBarchart(chooseCategory);
            }
        });
        checkBox_heating_and_cooling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckColor(1);
                chooseCategory="냉난방";
                managerCategoryBarchart(chooseCategory);
            }
        });
        checkBox_dangerous_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckColor(2);
                chooseCategory="위험물";
                managerCategoryBarchart(chooseCategory);
            }
        });
        checkBox_classroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckColor(3);
                chooseCategory="강의실";
                managerCategoryBarchart(chooseCategory);
            }
        });
        checkBox_laboratory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckColor(4);
                chooseCategory="연구실";
                managerCategoryBarchart(chooseCategory);
            }
        });
        checkBox_dormitory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckColor(5);
                chooseCategory="기숙사";
                managerCategoryBarchart(chooseCategory);
            }
        });
        checkBox_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckColor(6);
                chooseCategory="기타";
                managerCategoryBarchart(chooseCategory);
            }
        });
        checkBox_whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckColor(7);
                chooseCategory="전체";
                managerCategoryBarchart(chooseCategory);
            }
        });

        return root;
    }

    //사용자 처리 상태 표 레트로핏
    public void userStateTable(){
        retrofitClient= RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.showMemberState(savedMemberDto.getAccountId()).enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if(response.isSuccessful())
                {
                    List<Integer> memberStateList = response.body();
                    settingBeforeCheck.setText(String.valueOf(memberStateList.get(0)));
                    settingAfterCheck.setText(String.valueOf(memberStateList.get(1)));
                    settingProcessing.setText(String.valueOf(memberStateList.get(2)));
                    settingProcessedComplete.setText(String.valueOf(memberStateList.get(3)));
                }
                else{
                    Log.e("userStateTableRetrofit", String.valueOf(response.code()));
                    Toast.makeText(context,"다시 시도해주십시오",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });
    }
    //관리자 처리 상태 표 레트로핏
    public void managerStateTable(){
        retrofitClient= RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.showState().enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if(response.isSuccessful()){
                    List<Integer> stateList = response.body();
                    Log.e("managerState", String.valueOf(stateList.get(0)));
                    settingBeforeCheck.setText(String.valueOf(stateList.get(0)));
                    settingAfterCheck.setText(String.valueOf(stateList.get(1)));
                    settingProcessing.setText(String.valueOf(stateList.get(2)));
                    settingProcessedComplete.setText(String.valueOf(stateList.get(3)));
                    BarChartGrape(stateList);
                }else{
                    Log.e("userStateTableRetrofit", String.valueOf(response.code()));
                    Toast.makeText(context,"다시 시도해주십시오",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });

    }

    //관리자 상태 그래프 레트로핏
    public void managerCategoryBarchart(String category){
        retrofitClient= RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.showStateAndCategoryBoardList(category).enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if(response.isSuccessful()){
                    List<Integer>stateList=response.body();
                    BarChartGrape(stateList);
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {

            }
        });
    }

    //그래프
    public void BarChartGrape(List<Integer>stateList){
        barChart.clear();
        ArrayList<BarEntry> entries_before_read = new ArrayList<>();
        ArrayList<BarEntry> entries_after_read = new ArrayList<>();
        ArrayList<BarEntry> entries_processing = new ArrayList<>();
        ArrayList<BarEntry> entries_processed_complete = new ArrayList<>();

        entries_before_read.add(new BarEntry(1,stateList.get(0)));
        entries_after_read.add(new BarEntry(2, stateList.get(1)));
        entries_processing.add(new BarEntry(3, stateList.get(2)));
        entries_processed_complete.add(new BarEntry(4, stateList.get(3)));

        BarDataSet barDataSet_before_read = new BarDataSet(entries_before_read, "접수 전");
        BarDataSet barDataSet_after_read = new BarDataSet(entries_after_read, "접수 중");
        BarDataSet barDataSet_processing = new BarDataSet(entries_processing, "처리 중");
        BarDataSet barDataSet_processed_complete = new BarDataSet(entries_processed_complete, "처리 완료");

        barDataSet_processed_complete.setAxisDependency(YAxis.AxisDependency.LEFT);
        barChart.setDescription(null);

        BarData data = new BarData(barDataSet_before_read, barDataSet_after_read, barDataSet_processing, barDataSet_processed_complete);


        barDataSet_before_read.setColor(Color.BLUE);
        barDataSet_after_read.setColor(Color.GREEN);
        barDataSet_processing.setColor(Color.YELLOW);
        barDataSet_processed_complete.setColor(Color.GRAY);


        barChart.setData(data);

        barChart.invalidate();
        barChart.setTouchEnabled(false);

    }

    public void setCheckColor(int i){
        if (i == 0){
            checkBox_elevator.setTextColor(Color.parseColor("#000000"));
            checkBox_heating_and_cooling.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dangerous_goods.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_classroom.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_laboratory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dormitory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_etc.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_whole.setTextColor(Color.parseColor("#8C8C8C"));
        }
        else if (i == 1){
            checkBox_elevator.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_heating_and_cooling.setTextColor(Color.parseColor("#000000"));
            checkBox_dangerous_goods.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_classroom.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_laboratory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dormitory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_etc.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_whole.setTextColor(Color.parseColor("#8C8C8C"));
        }
        else if (i == 2){
            checkBox_elevator.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_heating_and_cooling.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dangerous_goods.setTextColor(Color.parseColor("#000000"));
            checkBox_classroom.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_laboratory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dormitory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_etc.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_whole.setTextColor(Color.parseColor("#8C8C8C"));
        }
        else if (i == 3){
            checkBox_elevator.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_heating_and_cooling.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dangerous_goods.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_classroom.setTextColor(Color.parseColor("#000000"));
            checkBox_laboratory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dormitory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_etc.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_whole.setTextColor(Color.parseColor("#8C8C8C"));
        }
        else if (i == 4){
            checkBox_elevator.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_heating_and_cooling.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dangerous_goods.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_classroom.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_laboratory.setTextColor(Color.parseColor("#000000"));
            checkBox_dormitory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_etc.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_whole.setTextColor(Color.parseColor("#8C8C8C"));
        }
        else if (i == 5){
            checkBox_elevator.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_heating_and_cooling.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dangerous_goods.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_classroom.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_laboratory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dormitory.setTextColor(Color.parseColor("#000000"));
            checkBox_etc.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_whole.setTextColor(Color.parseColor("#8C8C8C"));
        }
        else if (i == 6){
            checkBox_elevator.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_heating_and_cooling.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dangerous_goods.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_classroom.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_laboratory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dormitory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_etc.setTextColor(Color.parseColor("#000000"));
            checkBox_whole.setTextColor(Color.parseColor("#8C8C8C"));
        }
        else if (i == 7){
            checkBox_elevator.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_heating_and_cooling.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dangerous_goods.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_classroom.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_laboratory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_dormitory.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_etc.setTextColor(Color.parseColor("#8C8C8C"));
            checkBox_whole.setTextColor(Color.parseColor("#000000"));
        }

    }
}