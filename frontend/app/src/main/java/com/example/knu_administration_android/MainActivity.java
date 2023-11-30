package com.example.knu_administration_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private NotificationFragment notificationFragment;
    private RequestFragment requestFragment;
    private SettingFragment settingFragment;
    private LoginFragment loginFragment;
    private NavigationBarView navigationBarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        notificationFragment = new NotificationFragment();
        requestFragment = new RequestFragment();
        settingFragment = new SettingFragment();
        loginFragment = new LoginFragment();
        navigationBarView = findViewById(R.id.bottomNavigationView);

        if(!loginFragment.getIsLoggedIn())
        {
            Log.i("메인 login 상태",String.valueOf(loginFragment.getIsLoggedIn()));
            navigationBarView.setVisibility(View.GONE);
            showLoginFragment();
        }
        else{
            Log.i("메인 login 상태",String.valueOf(loginFragment.getIsLoggedIn()));
            navigationBarView.setVisibility(View.VISIBLE);
            onLoginSuccess();
        }

    }
    public void showLoginFragment()
    {
        FragmentManager loginFragmentManager = getSupportFragmentManager();
        FragmentTransaction loginFragmentTransaction = loginFragmentManager.beginTransaction();
        loginFragmentTransaction.replace(R.id.frameLayout,loginFragment).commit();
    }
    public void onLoginSuccess()
    {
        Log.i("메소드 login 상태",String.valueOf(loginFragment.getIsLoggedIn()));
        FragmentManager removeLoginFragmentManager = getSupportFragmentManager();
        FragmentTransaction removeLoginTransaction = removeLoginFragmentManager.beginTransaction();
        removeLoginTransaction.remove(loginFragment).commit();

        FragmentManager loginSuccessFragmentManager=getSupportFragmentManager();
        FragmentTransaction loginSuccessFragmentTransaction = loginSuccessFragmentManager.beginTransaction();
        loginSuccessFragmentTransaction.replace(R.id.frameLayout,requestFragment).commit();

        navigationBarView.setSelectedItemId(R.id.requestItem);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.notificationItem) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, notificationFragment).commit();
                    return true;
                } else if (itemId == R.id.requestItem) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, requestFragment).commit();
                    return true;
                } else if (itemId == R.id.settingItem) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, settingFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }

}