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
import android.widget.ImageButton;
import android.widget.TextView;

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

public class SettingMyHasteFragment extends Fragment {
    private ImageButton backBtn;
    private Context context;
    private TextView requestTextView;
    private RecyclerView recyclerView;
    private RequestRecyclerViewAdapter requestRecyclerViewAdapter;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_setting_my_haste, container, false);
        backBtn =root.findViewById(R.id.setting_haste_back_btn);
        recyclerView=root.findViewById(R.id.setting_my_haste_recyclerview);
        MemberDto savedMemberDto = UserPreferences.getUserInfo(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        myHasteResponse(savedMemberDto.getAccountId());
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return root;
    }

    public void myHasteResponse(String accountId){
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.showMemberHasteBoard(accountId).enqueue(new Callback<List<BoardDto>>() {
            @Override
            public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                if(response.isSuccessful()){
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