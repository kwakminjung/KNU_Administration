package com.example.knu_administration_android.request;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.knu_administration_android.dto.HasteDto;
import com.example.knu_administration_android.dto.MemberDto;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;

import com.example.knu_administration_android.RequestFragment;

import com.google.android.material.navigation.NavigationBarView;

import com.example.knu_administration_android.LocalDB.UserPreferences;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {
    private Context context;
    private ImageButton backBtn;
    private AppCompatButton changeBtn, deleteBtn;
    private ImageView stateStep1Iv, stateStep2Iv, stateStep3Iv;
    private TextView titleTv, writerIdTv, boardDateTv, boardContentTv;
    private RadioGroup categoryRG;
    private RadioButton category_elevator, category_heating_and_cooling, category_dangerous_goods,
            category_laboratory, category_classroom, category_dormitory, category_etc;
    private AppCompatButton hasteBtn;
    private TextView hasteCount;

    private TextView boardView;
    private Long boardIdFromRecyclerView;
    private BoardDto specificBoardInfo;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;
    private MemberDto savedMemberDto;
    private Bundle bundle;
    private Bundle specificBoardInfoBundle;
    private BoardViewModel boardViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();
        if (bundle != null) {
            boardIdFromRecyclerView = bundle.getLong("RecyclerKey");
        }
        boardViewModel = new ViewModelProvider(this).get(BoardViewModel.class);

    }

    public void getBoardSpecificInfo(Long boardIdFromRecyclerView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Long boardId = boardIdFromRecyclerView;
                retrofitClient = RetrofitClient.getInstance();
                retrofitAPI = RetrofitClient.getRetrofitInterface();

                try {
                    Response<BoardDto> response = retrofitAPI.showBoard(boardId).execute();
                    if (response.isSuccessful()) {
                        BoardDto result = response.body();
                        specificBoardInfo = result;
                        specificBoardInfoBundle = new Bundle();
                        specificBoardInfoBundle.putSerializable("boardSpecificInfo", specificBoardInfo);

                        // UI 업데이트를 위해 메인 스레드로 전환
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boardViewModel.setSpecificBoardInfo(specificBoardInfo);
                                updateUI();
                            }
                        });
                    } else {
                        // UI 업데이트를 위해 메인 스레드로 전환
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e("threads", "연결에 실패했습니다: " + e.getMessage());

                    // UI 업데이트를 위해 메인 스레드로 전환
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    public void onResume() {
        super.onResume();
        boardViewModel.getSpecificBoardInfo().observe(getViewLifecycleOwner(), specificBoardInfo -> {
        updateUI();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_board, container, false);
        context = container.getContext();
        getBoardSpecificInfo(boardIdFromRecyclerView);
        hasteBtn = root.findViewById(R.id.request_board_haste_btn);
        hasteCount = root.findViewById(R.id.haste_count_tv);
        savedMemberDto= UserPreferences.getUserInfo(getContext());
        changeBtn = root.findViewById(R.id.change_btn);
        backBtn = root.findViewById(R.id.back_btn);
        //카테고리 라디오 버튼
        categoryRG = root.findViewById(R.id.category_radioGroup);
        category_elevator = root.findViewById(R.id.category_elevator);
        category_heating_and_cooling = root.findViewById(R.id.category_heating_and_cooling);
        category_dangerous_goods = root.findViewById(R.id.category_dangerous_goods);
        category_laboratory = root.findViewById(R.id.category_laboratory);
        category_classroom = root.findViewById(R.id.category_classroom);
        category_dormitory = root.findViewById(R.id. category_dormitory);
        category_etc = root.findViewById(R.id.category_etc);
        boardView=root.findViewById(R.id.board_view_tv);
        titleTv = root.findViewById(R.id.board_title_tv);
        writerIdTv = root.findViewById(R.id.writer_id_tv);
        boardDateTv = root.findViewById(R.id.board_date_tv);
        boardContentTv = root.findViewById(R.id.board_content_tv);
        stateStep1Iv = root.findViewById(R.id.step1_iv);
        stateStep2Iv = root.findViewById(R.id.step2_iv);
        stateStep3Iv = root.findViewById(R.id.step3_iv);
        deleteBtn=root.findViewById(R.id.delete_btn);
        updateUI();
        return root;
    }

    public void updateUI(){
        if(specificBoardInfo!=null){

            // 하단 navi 숨김
            hideBottomNavigation(true);
            // 접수 상태로 들어갔거나 게시글의 사용자가 아닌 경우에 수정 버튼 없애기
            if(!savedMemberDto.getAccountId().equals(getResources().getString(R.string.managerId)))
            {
                if(specificBoardInfo.getState() != 0 || !specificBoardInfo.getWriterId().equals(savedMemberDto.getAccountId())) {
                    changeBtn.setVisibility(View.GONE);
                }

            }

            // backBtn 버튼 구현
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            }); // end of backBtn 버튼 구현

            changeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                    if(savedMemberDto.getAccountId().equals(getString(R.string.managerId))){
                        ManageModifyFragment manageModifyFragment = new ManageModifyFragment();
                        manageModifyFragment.setArguments(specificBoardInfoBundle);
                        fragmentManager.beginTransaction()
                                .addToBackStack("BoardFragment")
                                .replace(R.id.frameLayout, manageModifyFragment)
                                .commit();
                    } else {
                        WriteModifyFragment writemodifyfragment = new WriteModifyFragment();
                        writemodifyfragment.setArguments(specificBoardInfoBundle);
                        fragmentManager.beginTransaction()
                                .addToBackStack("BoardFragment")
                                .replace(R.id.frameLayout, writemodifyfragment)
                                .commit();
                    }
                }
            }); // end of changeBtn 구현

            categorySetting(specificBoardInfo.getCategory());

            //급해요 수
            hasteCount.setText(String.valueOf(specificBoardInfo.getHasteNum()));

            //급해요 버튼
            hasteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHaste();
                }
            });

            //조회수
            boardView.setText("조회 "+String.valueOf(specificBoardInfo.getView()));

            // Board 내용 넣기 (TextView : titleTv, writerId, boardDateTv, boardContentTv)
            titleTv.setText(specificBoardInfo.getTitle());
            if(specificBoardInfo.getWriterId().equals("(알 수 없음)"))
            {
                writerIdTv.setText(specificBoardInfo.getWriterId());
            }
            else {
                char[] writer = specificBoardInfo.getWriterId().toCharArray();
                for (int i = 0; i < writer.length; i++) {
                    if (3 <= i && i <= writer.length - 2) writer[i] = '*';
                }
                writerIdTv.setText( new String(writer));
            }

            boardDateTv.setText(specificBoardInfo.getDate());

            //수정 됐는지 확인 해야함
            if(specificBoardInfo.getIsModified().equals("수정됨")) {
                boardDateTv.setText("(수정됨)" + specificBoardInfo.getDate());
            } else{
                boardDateTv.setText(specificBoardInfo.getDate());
            }

            boardContentTv.setText(specificBoardInfo.getContent().replace(" ", "\u00A0")); // end of Board 내용 넣기

            // 민원 처리 상태
            stateSetting(specificBoardInfo.getState());

            //작성자인데 접수전일 경우, 관리자일 경우에만 버튼이 보임
            if(!(savedMemberDto.getAccountId().equals(getString(R.string.managerId))||(savedMemberDto.getAccountId().equals(specificBoardInfo.getWriterId())&&specificBoardInfo.getState()==0))){
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteBoard();
                }
            });
        }

    }

    public void deleteBoard(){
        retrofitClient=RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.deleteBoard(specificBoardInfo.getBoardId()).enqueue(new Callback<BoardDto>() {
            @Override
            public void onResponse(Call<BoardDto> call, Response<BoardDto> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context,"게시글이 삭제되었습니다",Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
                else{
                    Log.e("deleteRetrofit", String.valueOf(response.code()));
                    Toast.makeText(context,"다시 시도해주십시오",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BoardDto> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });
    }

    // 하단 navi 숨김 함수(bool)
    public void hideBottomNavigation(Boolean bool) {
        NavigationBarView navigationBarView = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool)
            navigationBarView.setVisibility(View.GONE);
        else
            navigationBarView.setVisibility(View.VISIBLE);
    }

    // 프레그먼트 벗어날 시에 하단 navi 보임
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideBottomNavigation(false);
    }

    public void stateSetting(int state){
        if(state==0){
            stateStep1Iv.setImageResource(R.drawable.board_state_step1_off_64);
            stateStep2Iv.setImageResource(R.drawable.board_state_step2_off_64);
            stateStep3Iv.setImageResource(R.drawable.board_state_step3_off_64);
        }
        if(state == 1) {
            stateStep1Iv.setImageResource(R.drawable.board_state_step1_on_64);
            stateStep2Iv.setImageResource(R.drawable.board_state_step2_off_64);
            stateStep3Iv.setImageResource(R.drawable.board_state_step3_off_64);
        } else if(state == 2) {
            stateStep1Iv.setImageResource(R.drawable.board_state_step1_off_64);
            stateStep2Iv.setImageResource(R.drawable.board_state_step2_on_64);
            stateStep3Iv.setImageResource(R.drawable.board_state_step3_off_64);
        } else if(state== 3) {
            stateStep1Iv.setImageResource(R.drawable.board_state_step1_off_64);
            stateStep2Iv.setImageResource(R.drawable.board_state_step2_off_64);
            stateStep3Iv.setImageResource(R.drawable.board_state_step3_on_64);
        }

    }

    public void categorySetting(String category){
        if(category!=null){
            if(category.equals("승강기"))
            {
                category_elevator.setChecked(true);
            }
            else if(category.equals("냉난방")){
                category_heating_and_cooling.setChecked(true);
            }
            else if(category.equals("위험물")){
                category_dangerous_goods.setChecked(true);
            }
            else if(category.equals("연구실")){
                category_laboratory.setChecked(true);
            }
            else if(category.equals("강의실")){
                category_classroom.setChecked(true);
            }
            else if(category.equals("기숙사")){
                category_dormitory.setChecked(true);
            }
            else if(category.equals("기타")){
                category_etc.setChecked(true);
            }
        }


    }

    public void isHaste(){
        retrofitClient=RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.showHaste(savedMemberDto.getMemberId(), specificBoardInfo.getBoardId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.e("haste",String.valueOf(response.code()));
                if(response.isSuccessful()){
                    Boolean isHaste=response.body();
                    HasteDto hasteDto = new HasteDto();
                    hasteDto.setBoardId(specificBoardInfo.getBoardId());
                    hasteDto.setMemberId(savedMemberDto.getMemberId());
                    if(isHaste){
                        deleteHaste(hasteDto);
                    }
                    else{
                        plusHaste(hasteDto);
                    }
                }
                else {
                    Toast.makeText(context,"다시 시도해주세요",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });
    }

    public void plusHaste(HasteDto hasteDto){
        retrofitClient=RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();

        retrofitAPI.insertHaste(hasteDto).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    int hasteNum =response.body();
                    Toast.makeText(context,"이 게시물에 '급해요'를 눌렀습니다",Toast.LENGTH_SHORT).show();
                    hasteCount.setText(String.valueOf(hasteNum));
                }
                else{
                    Toast.makeText(context,"다시 시도해주십시오",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });
    }
    public void deleteHaste(HasteDto hasteDto){
        retrofitClient=RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.removeHaste(hasteDto).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful())
                {
                    int hasteNum=response.body();
                    Toast.makeText(context,"이 게시물의 '급해요'를 취소했습니다",Toast.LENGTH_SHORT).show();
                    hasteCount.setText(String.valueOf(hasteNum));

                }
                else{
                    Toast.makeText(context,"다시 시도해주십시오",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });
    }

}