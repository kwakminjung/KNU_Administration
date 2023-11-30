package com.example.knu_administration_android.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_administration_android.R;
import com.example.knu_administration_android.dto.AlarmResponseDto;
import com.example.knu_administration_android.request.BoardFragment;
import com.example.knu_administration_android.request.RequestRecyclerViewAdapter;

import org.w3c.dom.Text;

import java.util.List;

public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmRecyclerViewAdapter.ViewHolder> {

    private List<AlarmResponseDto> alarmList;

    public AlarmRecyclerViewAdapter(List<AlarmResponseDto> alarmList) {
        this.alarmList=alarmList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.user_alarm_recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AlarmResponseDto alarmResponseDto = alarmList.get(position);
        holder.setItem(alarmResponseDto);
        holder.alarmContentTv.setText(alarmResponseDto.getNoticeTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("RecyclerKey", alarmResponseDto.getBoardId());
                BoardFragment boardFragment = new BoardFragment();
                boardFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, boardFragment)
                        .addToBackStack("AlarmRecyclerView")
                        .commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView alarmContentTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmContentTv=itemView.findViewById(R.id.alarm_content);
        }

        public void setItem(AlarmResponseDto item) {
            alarmContentTv.setText(item.getNoticeTitle());
        }

    }

}
