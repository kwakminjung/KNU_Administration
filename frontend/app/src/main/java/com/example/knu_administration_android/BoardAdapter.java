package com.example.knu_administration_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_administration_android.object.Board;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    private ArrayList<Board> boardsList;

    @NonNull
    @Override
    public BoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_setting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardAdapter.ViewHolder holder, int position) {
        holder.onBind(boardsList.get(position));
    }

    public void setBoardsList(ArrayList<Board> list){
        this.boardsList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return boardsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.setting_item_title);
            context = (TextView) itemView.findViewById(R.id.setting_item_context);
        }

        void onBind(Board item){
            title.setText(item.getTitle());
            context.setText(item.getContent());
        }
    }
}