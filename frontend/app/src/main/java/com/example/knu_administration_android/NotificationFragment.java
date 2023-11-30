package com.example.knu_administration_android;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.knu_administration_android.notification.NotificationData;
import com.example.knu_administration_android.notification.NotificationRecyclerViewAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.annotation.Documented;

public class NotificationFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificationRecyclerViewAdapter notificationRecyclerViewAdapter;
    private Context context;
    private String URL = "https://www.kongju.ac.kr/kongju/12499/subview.do";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_notification, container, false);
        context = container.getContext();
      
        recyclerView = root.findViewById(R.id.notification_board_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        notificationRecyclerViewAdapter = new NotificationRecyclerViewAdapter();

        //크롤링 코드
        new Thread() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(URL).get();
                    Elements data = doc.select(".board-table.horizon1 tbody tr");
                    for (Element element : data) {
                        String title = element.select(".td-subject a strong").text();
                        String access = "조회수: " + element.select(".td-access").text();
                        String writer = element.select(".td-write").text();
                        String date = element.select(".td-date").text();
                        String href = "https://www.kongju.ac.kr" + element.select("a").attr("href")
                                + "?layout=unknown";
                        notificationRecyclerViewAdapter.addItem(new NotificationData(title, access, writer, date, href));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //프레그먼트가 작업을 마칠때도 호출되면 오류가 발생할 수 있으므로 null인지 확인하고 아닐 때만 호출할 것
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                            recyclerView.addItemDecoration(dividerItemDecoration);
                            recyclerView.setAdapter(notificationRecyclerViewAdapter);
                        }
                    });
                }
            }
        }.start();

        return root;
    }
}