package com.example.knu_administration_android.request;

import android.content.Context;
import android.os.Bundle;

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
import com.example.knu_administration_android.RequestFragment;
import com.example.knu_administration_android.dto.BoardDto;
import com.example.knu_administration_android.dto.MemberDto;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;
import com.google.android.material.navigation.NavigationBarView;

import com.example.knu_administration_android.LocalDB.UserPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteRequestFragment extends Fragment {
    private ImageButton backBtn;
    private Button doneBtn;
    private EditText titleET;
    private EditText contentET;
    private Context context;

    private RadioGroup categoryRG;
    private RadioButton category_elevator, category_heating_and_cooling, category_dangerous_goods,
            category_laboratory, category_classroom, category_dormitory, category_etc;

    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;

    private static String chooseCategory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_write_request, container, false);
        context = container.getContext();
        backBtn=root.findViewById(R.id.back_btn);
        doneBtn=root.findViewById(R.id.write_done_btn);
        titleET=root.findViewById(R.id.request_write_title);
        contentET=root.findViewById(R.id.request_write_content);

        //카테고리 라디오 버튼
        categoryRG = root.findViewById(R.id.category_radioGroup);
        category_elevator = root.findViewById(R.id.category_elevator);
        category_heating_and_cooling = root.findViewById(R.id.category_heating_and_cooling);
        category_dangerous_goods = root.findViewById(R.id.category_dangerous_goods);
        category_laboratory = root.findViewById(R.id.category_laboratory);
        category_classroom = root.findViewById(R.id.category_classroom);
        category_dormitory = root.findViewById(R.id.category_dormitory);
        category_etc = root.findViewById(R.id.category_etc);

        //카테고리 맨 처음 설정
        category_elevator.setChecked(true);
        chooseCategory="승강기";
        
        //카테고리 선택
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

        //네비게이션 바 숨기기
        hideBottomNavigation(true);

        // backBtn 버튼 구현
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, new RequestFragment())
                        .commit();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=titleET.getText().toString();
                String content = contentET.getText().toString();
                MemberDto savedMemberDto = UserPreferences.getUserInfo(getContext());
                BoardDto boardDto = new BoardDto();
                boardDto.setBoardId(Long.valueOf(0));
                boardDto.setTitle(title);
                boardDto.setContent(content);
                boardDto.setState(0);
                boardDto.setDate(null);
                boardDto.setWriterId(savedMemberDto.getAccountId());
                boardDto.setView(0);
                boardDto.setIsModified(null);
                boardDto.setHasteNum(0);
                boardDto.setCategory(chooseCategory);
                retrofitClient = RetrofitClient.getInstance();
                retrofitAPI=RetrofitClient.getRetrofitInterface();
                retrofitAPI.createBoard(boardDto).enqueue(new Callback<BoardDto>() {
                    @Override
                    public void onResponse(Call<BoardDto> call, Response<BoardDto> response) {
                        if (response.isSuccessful()) {
                            // 성공적으로 요청이 처리됐을 때의 동작
                            Toast.makeText(getActivity(), "작성이 완료되었습니다", Toast.LENGTH_SHORT).show();
                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            fragmentManager.popBackStack();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frameLayout, new RequestFragment())
                                    .commit();
                        } else {
                            Log.e("retrofit", String.valueOf(response.code()));
                            Toast.makeText(getActivity(), "다시 시도해주십시오", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<BoardDto> call, Throwable t) {
                        Log.e("retrofit", t.toString() );
                    }
                });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideBottomNavigation(false);
    }


}