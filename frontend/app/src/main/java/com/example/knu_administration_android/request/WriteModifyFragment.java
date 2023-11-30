package com.example.knu_administration_android.request;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.knu_administration_android.R;
import com.example.knu_administration_android.dto.BoardDto;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteModifyFragment extends Fragment {

    private ImageButton backBtn;
    private Button doneBtn;
    private EditText titleET, contentET;
    private Context context;
    private BoardDto specificBoardInfo;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;
    private static String chooseCategory;
    private RadioGroup categoryRG;
    private RadioButton category_elevator, category_heating_and_cooling, category_dangerous_goods,
            category_laboratory, category_classroom, category_dormitory, category_etc;

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
        View root =inflater.inflate(R.layout.fragment_write_modify, container, false);
        context = container.getContext();
        backBtn=root.findViewById(R.id.back_btn);
        doneBtn=root.findViewById(R.id.modify_done_btn);
        titleET=root.findViewById(R.id.request_write_title);
        contentET=root.findViewById(R.id.request_write_content);

        titleET.setText(specificBoardInfo.getTitle());
        contentET.setText(specificBoardInfo.getContent());


        //네비게이션 바 숨기기
        hideBottomNavigation(true);

        // backBtn 버튼 구현
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        //카테고리 라디오 버튼
        categoryRG = root.findViewById(R.id.category_radioGroup);
        category_elevator = root.findViewById(R.id.category_elevator);
        category_heating_and_cooling = root.findViewById(R.id.category_heating_and_cooling);
        category_dangerous_goods = root.findViewById(R.id.category_dangerous_goods);
        category_laboratory = root.findViewById(R.id.category_laboratory);
        category_classroom = root.findViewById(R.id.category_classroom);
        category_dormitory = root.findViewById(R.id.category_dormitory);
        category_etc = root.findViewById(R.id.category_etc);

        //기존에 선택된 카테고리 표시
        if(specificBoardInfo.getCategory().equals("승강기"))
        {
            category_elevator.setChecked(true);
            chooseCategory="승강기";
        }
        else if(specificBoardInfo.getCategory().equals("냉난방"))
        {
            category_heating_and_cooling.setChecked(true);
            chooseCategory="냉난방";
        }
        else if(specificBoardInfo.getCategory().equals("위험물")){
            category_dangerous_goods.setChecked(true);
            chooseCategory="위험물";
        }
        else if(specificBoardInfo.getCategory().equals("연구실")){
            category_laboratory.setChecked(true);
            chooseCategory="연구실";
        }
        else if(specificBoardInfo.getCategory().equals("강의실")){
            category_classroom.setChecked(true);
            chooseCategory="강의실";
        }
        else if(specificBoardInfo.getCategory().equals("기숙사")){
            category_dormitory.setChecked(true);
            chooseCategory="기숙사";
        }
        else if(specificBoardInfo.getCategory().equals("기타")){
            category_etc.setChecked(true);
            chooseCategory="기타";
        }


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

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=titleET.getText().toString();
                String content = contentET.getText().toString();
                String category=chooseCategory;
                if(title.equals(specificBoardInfo.getTitle())&&content.equals(specificBoardInfo.getContent())&&category.equals(specificBoardInfo.getCategory())){
                    Toast.makeText(getActivity(), "변경된 사항 없음", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
                else{
                    userModifyBoard(title, content, category);
                }
            }
        });

        return root;
    }
    public void hideBottomNavigation(Boolean bool) {
        NavigationBarView navigationBarView = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool)
            navigationBarView.setVisibility(View.GONE);
        else
            navigationBarView.setVisibility(View.VISIBLE);
    }
    public void userModifyBoard(String title, String content, String category){
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardId(specificBoardInfo.getBoardId());
        boardDto.setTitle(title);
        boardDto.setContent(content);
        boardDto.setCategory(category);
        boardDto.setDate(specificBoardInfo.getDate());
        boardDto.setIsModified(specificBoardInfo.getIsModified());
        boardDto.setState(specificBoardInfo.getState());
        boardDto.setHasteNum(specificBoardInfo.getHasteNum());
        boardDto.setView(specificBoardInfo.getView());
        boardDto.setWriterId(specificBoardInfo.getWriterId());
        specificBoardInfo.setTitle(boardDto.getTitle());
        specificBoardInfo.setContent(boardDto.getContent());
        specificBoardInfo.setCategory(boardDto.getCategory());
        retrofitClient= RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.updateBoard(boardDto).enqueue(new Callback<BoardDto>() {
            @Override
            public void onResponse(Call<BoardDto> call, Response<BoardDto> response) {
                if(response.isSuccessful()){
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    Toast.makeText(getActivity(), "변경 완료", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.e("retrofit",String.valueOf(response.code()));
                    Toast.makeText(getActivity(), "다시 시도해주십시오", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BoardDto> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });
    }
}