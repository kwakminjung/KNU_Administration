package com.example.knu_administration_android;

import static android.view.View.GONE;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knu_administration_android.LocalDB.UserPreferences;
import com.example.knu_administration_android.dto.BoardDto;
import com.example.knu_administration_android.dto.MemberDto;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;
import com.example.knu_administration_android.request.RequestRecyclerViewAdapter;
import com.example.knu_administration_android.request.SearchFragment;
import com.example.knu_administration_android.request.WriteRequestFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestFragment extends Fragment {
    private RecyclerView recyclerView;
    private RequestRecyclerViewAdapter requestRecyclerViewAdapter;
    private Context context;
    private TextView latestOrderBtn;
    private TextView oldOrderBtn;
    private TextView hasteBtn;
    private TextView viewerBtn;
    private FloatingActionButton writeRequestBtn;
    private ImageButton searchBtn;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;
    private MemberDto savedMemberDto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        hideBottomNavigation(false);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_request, container, false);
        context = container.getContext();
        recyclerView=root.findViewById(R.id.request_board_recyclerView);
        latestOrderBtn=root.findViewById(R.id.request_board_latest_order_btn);
        oldOrderBtn=root.findViewById(R.id.request_board_old_order_btn);
        hasteBtn = root.findViewById(R.id.request_board_haste_order_btn);
        viewerBtn = root.findViewById(R.id.request_board_view_order_btn);
        writeRequestBtn=root.findViewById(R.id.request_write_btn);
        searchBtn = root.findViewById(R.id.request_board_search_btn);
        savedMemberDto= UserPreferences.getUserInfo(getContext());

        latestBoardResponse();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //최신 순 정렬 버튼
        latestOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOrderColor(0);
                latestBoardResponse();
            }
        });

        //오래된 순 정렬 버튼
        oldOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOrderColor(1);
                oldestBoardResponse();

            }
        });

        //급해요 순 정렬 버튼
        hasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOrderColor(2);
                hasteBoardResponse();
            }
        });

        //조회수 순 정렬
        viewerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOrderColor(3);
                viewBoardResponse();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .addToBackStack("requestFragment")
                        .replace(R.id.frameLayout, new SearchFragment())
                        .commit();
            }
        });
        if(savedMemberDto.getAccountId().equals(getString(R.string.managerId))){
            writeRequestBtn.setVisibility(View.GONE);
        }
        writeRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack("RequestFragment")
                        .replace(R.id.frameLayout, new WriteRequestFragment())
                        .commit();
            }
        });
        return root;
    }

    public void setOrderColor(int key) {
        if (key == 0) {
            latestOrderBtn.setTextColor(Color.parseColor("#000000"));
            oldOrderBtn.setTextColor(Color.parseColor("#8C8C8C"));
            hasteBtn.setTextColor(Color.parseColor("#8C8C8C"));
            viewerBtn.setTextColor(Color.parseColor("#8C8C8C"));
        } else if (key == 1) {
            latestOrderBtn.setTextColor(Color.parseColor("#8C8C8C"));
            oldOrderBtn.setTextColor(Color.parseColor("#000000"));
            hasteBtn.setTextColor(Color.parseColor("#8C8C8C"));
            viewerBtn.setTextColor(Color.parseColor("#8C8C8C"));
        } else if (key == 2) {
            latestOrderBtn.setTextColor(Color.parseColor("#8C8C8C"));
            oldOrderBtn.setTextColor(Color.parseColor("#8C8C8C"));
            hasteBtn.setTextColor(Color.parseColor("#000000"));
            viewerBtn.setTextColor(Color.parseColor("#8C8C8C"));
        } else if (key == 3) {
            latestOrderBtn.setTextColor(Color.parseColor("#8C8C8C"));
            oldOrderBtn.setTextColor(Color.parseColor("#8C8C8C"));
            hasteBtn.setTextColor(Color.parseColor("#8C8C8C"));
            viewerBtn.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void latestBoardResponse(){
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.getLatestBoardList().enqueue(new Callback<List<BoardDto>>() {
            @Override
            public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 최신 게시판 목록을 받아왔을 때의 처리
                    List<BoardDto> boardList = response.body();
                    requestRecyclerViewAdapter = new RequestRecyclerViewAdapter(boardList);
                    recyclerView.setAdapter(requestRecyclerViewAdapter);
                } else {
                    // 서버 응답이 실패한 경우의 처리
                    Log.e("retrofit", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BoardDto>> call, Throwable t) {
                Log.e("retrofit", t.toString() );

            }
        });

    }

    public void oldestBoardResponse(){
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.getOldBoardList().enqueue(new Callback<List<BoardDto>>() {
            @Override
            public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 오래된 순서대로 게시판 목록을 받아왔을 때의 처리
                    List<BoardDto> boardList = response.body();
                    requestRecyclerViewAdapter = new RequestRecyclerViewAdapter(boardList);
                    recyclerView.setAdapter(requestRecyclerViewAdapter);

                } else {
                    // 서버 응답이 실패한 경우의 처리
                    Log.e("retrofit", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BoardDto>> call, Throwable t) {
                Log.e("retrofit", t.toString() );
            }
        });
    }

    public void hasteBoardResponse(){
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.showHasteBoard().enqueue(new Callback<List<BoardDto>>() {
            @Override
            public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                if (response.isSuccessful()) {
                    List<BoardDto> boardList = response.body();
                    requestRecyclerViewAdapter = new RequestRecyclerViewAdapter(boardList);
                    recyclerView.setAdapter(requestRecyclerViewAdapter);

                } else {
                    // 서버 응답이 실패한 경우의 처리
                    Log.e("retrofit", "Error: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<BoardDto>> call, Throwable t) {
                Log.e("retrofit", t.toString() );
            }
        });
    }

    public void viewBoardResponse(){
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.showViewBoard().enqueue(new Callback<List<BoardDto>>() {
            @Override
            public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                if (response.isSuccessful()) {
                    List<BoardDto> boardList = response.body();
                    requestRecyclerViewAdapter = new RequestRecyclerViewAdapter(boardList);
                    recyclerView.setAdapter(requestRecyclerViewAdapter);

                } else {
                    // 서버 응답이 실패한 경우의 처리
                    Log.e("retrofit", "Error: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<BoardDto>> call, Throwable t) {
                Log.e("retrofit", t.toString() );
            }
        });
    }

    public void hideBottomNavigation(Boolean bool) {
        NavigationBarView navigationBarView = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool)
            navigationBarView.setVisibility(GONE);
        else
            navigationBarView.setVisibility(View.VISIBLE);
    }


}