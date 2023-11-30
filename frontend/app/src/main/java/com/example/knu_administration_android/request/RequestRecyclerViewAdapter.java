package com.example.knu_administration_android.request;


import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.knu_administration_android.R;
import com.example.knu_administration_android.dto.BoardDto;
import com.example.knu_administration_android.object.Board;

import java.util.List;

public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.ViewHolder>{
    private List<BoardDto> boardList;

    public RequestRecyclerViewAdapter(List<BoardDto> boardList) {
        this.boardList = boardList;
    }
    @NonNull
    @Override
    public RequestRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.request_recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BoardDto board = boardList.get(position);
        holder.setItem(board);
        holder.requestTitleTv.setText(board.getTitle());

        if(board.getState()==0) {holder.requestStateTv.setText("접수 전");}
        else if(board.getState()==1) {holder.requestStateTv.setText("접수 완료");}
        else if(board.getState()==2){holder.requestStateTv.setText("처리 중");}
        else if(board.getState()==3) {holder.requestStateTv.setText("처리 완료");}
        holder.requestTimeTv.setText(board.getDate());
        holder.requestViewTv.setText(String.valueOf(board.getView()));
        holder.requestHasteTv.setText(String.valueOf(board.getHasteNum()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("RecyclerKey", board.getBoardId());
                BoardFragment boardFragment = new BoardFragment();
                boardFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, boardFragment)
                        .addToBackStack("BoardRecyclerView")
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView requestTitleTv;
        TextView requestStateTv;
        TextView requestTimeTv;
        TextView requestHasteTv;
        TextView requestViewTv;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            requestTitleTv = itemView.findViewById(R.id.request_board_title);
            requestStateTv =itemView.findViewById(R.id.request_board_state);
            requestTimeTv = itemView.findViewById(R.id.request_board_time);
            requestHasteTv = itemView.findViewById(R.id.request_board_haste);
            requestViewTv =itemView.findViewById(R.id.request_board_view);
        }

        public void setItem(BoardDto item){
            requestTitleTv.setText(item.getTitle());
            requestStateTv.setText(String.valueOf(item.getState()));
            requestTimeTv.setText(item.getDate());
            requestHasteTv.setText("급해요 "+item.getHasteNum());
            requestViewTv.setText("조회수 "+item.getView());
        }
    }
}
