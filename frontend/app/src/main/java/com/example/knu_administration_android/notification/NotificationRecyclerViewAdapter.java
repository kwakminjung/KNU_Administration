package com.example.knu_administration_android.notification;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_administration_android.NotificationFragment;
import com.example.knu_administration_android.R;
import com.example.knu_administration_android.RequestFragment;

import java.util.ArrayList;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {

    ArrayList<NotificationData>items = new ArrayList<>();

    @NonNull
    @Override
    public NotificationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.notification_recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationData item = items.get(position);
        holder.setItem(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssdfs", item.getHref());
                String href = item.getHref();

                if (!TextUtils.isEmpty(href)) {
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();

                    WebViewFragment webViewFragment = new WebViewFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("url", href);
                    webViewFragment.setArguments(bundle);

                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, webViewFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(NotificationData item) {
        items.add(item);
    }

    public void setItems(ArrayList<NotificationData> items) {
        this.items = items;
    }

    public void setItem(int position, NotificationData item) {
        items.set(position, item);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitleTv;
        TextView notificationAccessTv;
        TextView notificationWriterTv;
        TextView notificationDateTv;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            notificationTitleTv = itemView.findViewById(R.id.notification_title);
            notificationAccessTv =itemView.findViewById(R.id.notification_access);
            notificationWriterTv = itemView.findViewById(R.id.notification_writer);
            notificationDateTv = itemView.findViewById(R.id.notification_date);
        }

        public void setItem(NotificationData item){
            notificationTitleTv.setText(item.getNotificationTitle().replace(" ", "\u00A0"));
            notificationAccessTv.setText(item.getNotificationAccess());
            notificationWriterTv.setText(item.getNotificationWriter());
            notificationDateTv.setText(item.getNotificationDate());
        }
    }
}
