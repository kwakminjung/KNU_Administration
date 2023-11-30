package com.example.knu_administration_android.setting;

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

// BoardAdapter 클래스가 어디서 온 것인지에 따라서 아래 import 문을 추가해야 합니다.
// import com.example.knu_administration_android.BoardAdapter;
import com.example.knu_administration_android.LocalDB.UserPreferences;
import com.example.knu_administration_android.R;
import com.example.knu_administration_android.dto.BoardDto;
import com.example.knu_administration_android.dto.MemberDto;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;
import com.example.knu_administration_android.request.RequestRecyclerViewAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingMyArticlesFragment extends Fragment {
    private Context context;
    private TextView requestTextView;
    private RecyclerView recyclerView;
    private RequestRecyclerViewAdapter requestRecyclerViewAdapter;
    private ImageButton backBtn;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting_my_articles, container, false);
        backBtn = root.findViewById(R.id.setting_articles_back_btn);
        recyclerView = root.findViewById(R.id.setting_my_articles_recyclerview);
        MemberDto savedMemberDto = UserPreferences.getUserInfo(getContext());
        myArticleResponse(savedMemberDto.getAccountId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        return root;
    }

    public void myArticleResponse(String accountId){
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.showMemberBoard(accountId).enqueue(new Callback<List<BoardDto>>() {
            @Override
            public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                if(response.isSuccessful())
                {
                    List<BoardDto> boardList = response.body();
                    requestRecyclerViewAdapter = new RequestRecyclerViewAdapter(boardList);
                    recyclerView.setAdapter(requestRecyclerViewAdapter);
                }
                else {
                    Log.e("retrofit", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BoardDto>> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });

    }
}
