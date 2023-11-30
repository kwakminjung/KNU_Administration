package com.example.knu_administration_android.request;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.knu_administration_android.R;
import com.example.knu_administration_android.RequestFragment;
import com.example.knu_administration_android.dto.BoardDto;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;
import com.example.knu_administration_android.object.Board;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private ImageButton searchBtn, backBtn;
    private EditText searchET;
    private TextView searchTV, searchResultTV;
    private RecyclerView recyclerView;
    private RequestRecyclerViewAdapter requestRecyclerViewAdapter;
    private Context context;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;

    @Override
    public void onResume() {
        super.onResume();
        hideBottomNavigation(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root =inflater.inflate(R.layout.fragment_search, container, false);
        context = container.getContext();
        backBtn = (ImageButton) root.findViewById(R.id.search_board_backBtn);
        searchTV = (TextView) root.findViewById(R.id.search_board_TextView);
        searchResultTV = (TextView) root.findViewById(R.id.search_board_result);
        searchBtn = (ImageButton) root.findViewById(R.id.search_board_search_btn);
        searchET = (EditText) root.findViewById(R.id.serach_board_search_editText);
        recyclerView = root.findViewById(R.id.seach_board_recyclerView);

        hideBottomNavigation(true);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 검색어로 위에 바꿔주기
                searchTV.setText(searchET.getText().toString());
                searchResultTV.setVisibility(View.VISIBLE);
                String searchWordReplaceBlank=searchET.getText().toString().replaceAll(" ","");
                searchResponse(searchWordReplaceBlank);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
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

    public void searchResponse(String keyword){
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.searchBoard(keyword).enqueue(new Callback<List<BoardDto>>() {
            @Override
            public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                if(response.isSuccessful()){
                    List<BoardDto> boardDtoList = response.body();
                    requestRecyclerViewAdapter=new RequestRecyclerViewAdapter(boardDtoList);
                    recyclerView.setAdapter(requestRecyclerViewAdapter);
                }
                else{
                    Log.e("retrofit", "Error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<BoardDto>> call, Throwable t) {
                Log.e("retrofit", t.toString() );
            }
        });
    }
}