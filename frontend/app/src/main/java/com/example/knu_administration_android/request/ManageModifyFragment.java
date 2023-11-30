package com.example.knu_administration_android.request;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knu_administration_android.R;
import com.example.knu_administration_android.dto.BoardDto;
import com.example.knu_administration_android.dto.BoardManagerUpdateDto;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageModifyFragment extends Fragment {

    private Context context;
    private ImageButton backBtn;
    private AppCompatButton completeBtn;
    private ImageView stateStep1Iv, stateStep2Iv, stateStep3Iv;
    private TextView titleTv, writerIdTv, boardDateTv, boardContentTv, boardViewTv;
    private RadioGroup categoryRG;
    private RadioButton category_elevator, category_heating_and_cooling, category_dangerous_goods,
            category_laboratory, category_classroom, category_dormitory, category_etc;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;
    private BoardDto specificBoardInfo;
    private static String chooseCategory;
    private static int chooseState;

    @Override
    public void onResume() {
        super.onResume();
        hideBottomNavigation(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        specificBoardInfo = new BoardDto();
        Bundle bundle = getArguments();
        if (bundle != null) {
            specificBoardInfo = (BoardDto) bundle.getSerializable("boardSpecificInfo");
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manage_modify, container, false);
        context = container.getContext();
        hideBottomNavigation(true);
        // backBtn 버튼 구현
        backBtn = root.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        }); // end of backBtn 버튼 구현

        // completeBtn 구현 : 수정 내용 저장
        completeBtn = root.findViewById(R.id.complete_btn);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chooseCategory.equals(specificBoardInfo.getCategory())&&chooseState== specificBoardInfo.getState()){
                    Toast.makeText(getActivity(), "변경된 사항 없음", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
                else{
                    BoardManagerUpdateDto boardManagerUpdateDto = new BoardManagerUpdateDto();
                    boardManagerUpdateDto.setCategory(chooseCategory);
                    boardManagerUpdateDto.setState(chooseState);
                    managerModifyRetrofit(specificBoardInfo.getBoardId(),boardManagerUpdateDto);
                }

            }
        });

        //카테고리 라디오 버튼
        categoryRG = root.findViewById(R.id.category_radioGroup);
        category_elevator = root.findViewById(R.id.category_elevator);
        category_heating_and_cooling = root.findViewById(R.id.category_heating_and_cooling);
        category_dangerous_goods = root.findViewById(R.id.category_dangerous_goods);
        category_laboratory = root.findViewById(R.id.category_laboratory);
        category_classroom = root.findViewById(R.id.category_classroom);
        category_dormitory = root.findViewById(R.id. category_dormitory);
        category_etc = root.findViewById(R.id.category_etc);

        chooseCategory= specificBoardInfo.getCategory();
        //기존에 선택된 카테고리 표시
        if(specificBoardInfo.getCategory().equals("승강기"))
        {
            category_elevator.setChecked(true);
        }
        else if(specificBoardInfo.getCategory().equals("냉난방"))
        {
            category_heating_and_cooling.setChecked(true);
        }
        else if(specificBoardInfo.getCategory().equals("위험물")){
            category_dangerous_goods.setChecked(true);
        }
        else if(specificBoardInfo.getCategory().equals("연구실")){
            category_laboratory.setChecked(true);
        }
        else if(specificBoardInfo.getCategory().equals("강의실")){
            category_classroom.setChecked(true);
        }
        else if(specificBoardInfo.getCategory().equals("기숙사")){
            category_dormitory.setChecked(true);
        }
        else if(specificBoardInfo.getCategory().equals("기타")){
            category_etc.setChecked(true);
        }


        //카테고리 색 변경
        categoryRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.category_elevator){
                    category_elevator.setChecked(true);
                    chooseCategory="승강기";

                }
                else if(i==R.id.category_heating_and_cooling){
                    category_heating_and_cooling.setChecked(true);
                    chooseCategory="냉난방";

                }
                else if(i==R.id.category_dangerous_goods){
                    category_dangerous_goods.setChecked(true);
                    chooseCategory="위험물";

                }
                else if(i==R.id.category_laboratory){
                    category_laboratory.setChecked(true);
                    chooseCategory="연구실";

                }
                else if(i==R.id.category_classroom){
                    category_classroom.setChecked(true);
                    chooseCategory="강의실";

                }
                else if(i==R.id.category_dormitory){
                    category_dormitory.setChecked(true);
                    chooseCategory="기숙사";

                }
                else if(i==R.id.category_etc){
                    category_etc.setChecked(true);
                    chooseCategory="기타";
                }
            }
        });

        // Board 내용 넣기 (TextView : titleTv, writerId, boardDateTv, boardContentTv)
        titleTv = root.findViewById(R.id.board_title_tv);
        titleTv.setText(specificBoardInfo.getTitle());

        writerIdTv = root.findViewById(R.id.writer_id_tv);
        if(specificBoardInfo.getWriterId()!=null)
        {
            char[] writer = specificBoardInfo.getWriterId().toCharArray();
            for(int i = 0 ; i < writer.length ; i++) {
                if(3 <= i && i <= writer.length - 2) writer[i] = '*';
            }
            writerIdTv.setText( new String(writer));
        }
        boardDateTv = root.findViewById(R.id.board_date_tv);
        //수정 됐는지 확인 해야함
        if(specificBoardInfo.getIsModified().equals("수정됨")) {
            boardDateTv.setText("(수정됨)" + specificBoardInfo.getDate());
        } else {
            boardDateTv.setText(specificBoardInfo.getDate());
        }

        //조회수
        boardViewTv=root.findViewById(R.id.board_view_tv);
        boardViewTv.setText("조회수: "+String.valueOf(specificBoardInfo.getView()));
        boardContentTv = root.findViewById(R.id.board_content_tv);
        if(specificBoardInfo.getContent()!=null)
        {
            boardContentTv.setText(specificBoardInfo.getContent().replace(" ", "\u00A0")); // end of Board 내용 넣기
        }

        // 민원 처리 상태
        stateStep1Iv = root.findViewById(R.id.step1_iv);
        stateStep2Iv = root.findViewById(R.id.step2_iv);
        stateStep3Iv = root.findViewById(R.id.step3_iv);
        chooseState=specificBoardInfo.getState();
        modifyBoardState(chooseState);
        stateStep1Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyBoardState(1);
                chooseState=1;
            }
        });

        stateStep2Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyBoardState(2);
                chooseState=2;
            }
        });

        stateStep3Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyBoardState(3);
                chooseState=3;
            }
        }); // end of 민원 처리 상태



        return root;
    }

    // 하단 navi 숨김 함수(bool)
    public void hideBottomNavigation(Boolean bool) {
        NavigationBarView navigationBarView = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool)
            navigationBarView.setVisibility(View.GONE);
        else
            navigationBarView.setVisibility(View.VISIBLE);
    }

    // 버튼 색깔
    private void modifyBoardState(int state) {
        if(state==0)
        {
            stateStep1Iv.setImageResource(R.drawable.board_state_step1_off_64);
            stateStep2Iv.setImageResource(R.drawable.board_state_step2_off_64);
            stateStep3Iv.setImageResource(R.drawable.board_state_step3_off_64);
        }
        else if(state == 1) {
            stateStep1Iv.setImageResource(R.drawable.board_state_step1_on_64);
            stateStep2Iv.setImageResource(R.drawable.board_state_step2_off_64);
            stateStep3Iv.setImageResource(R.drawable.board_state_step3_off_64);
        } else if(state == 2) {
            stateStep1Iv.setImageResource(R.drawable.board_state_step1_off_64);
            stateStep2Iv.setImageResource(R.drawable.board_state_step2_on_64);
            stateStep3Iv.setImageResource(R.drawable.board_state_step3_off_64);
        } else if(state == 3) {
            stateStep1Iv.setImageResource(R.drawable.board_state_step1_off_64);
            stateStep2Iv.setImageResource(R.drawable.board_state_step2_off_64);
            stateStep3Iv.setImageResource(R.drawable.board_state_step3_on_64);
        }
    }

    //
    private void managerModifyRetrofit(Long boardId, BoardManagerUpdateDto boardManagerUpdateDto){
        specificBoardInfo.setCategory(boardManagerUpdateDto.getCategory());
        specificBoardInfo.setState(boardManagerUpdateDto.getState());
        retrofitClient=RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.updateManagement(boardId, boardManagerUpdateDto).enqueue(new Callback<BoardDto>() {
            @Override
            public void onResponse(Call<BoardDto> call, Response<BoardDto> response) {
                if(response.isSuccessful()){
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    Toast.makeText(context,"변경 성공",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"다시 시도해주세요",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BoardDto> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });

    }



}