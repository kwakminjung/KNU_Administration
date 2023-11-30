package com.example.knu_administration_android.notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.knu_administration_android.R;
import com.google.android.material.navigation.NavigationBarView;

public class WebViewFragment extends Fragment {
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_web_view, container, false);
        webView = root.findViewById(R.id.webView);

        // 하단 navi 숨김
        hideBottomNavigation(true);

        String url = getArguments().getString("url");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowContentAccess(true);

        // WebViewClient 설정 (새 창이 열리지 않도록 함)
        webView.setWebViewClient(new WebViewClient());

        // URL 로드
        if (url != null) {
            webView.loadUrl(url);
        }

        return root;
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
}