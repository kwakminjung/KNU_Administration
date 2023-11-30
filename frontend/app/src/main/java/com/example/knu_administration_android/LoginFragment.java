package com.example.knu_administration_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knu_administration_android.LocalDB.UserPreferences;
import com.example.knu_administration_android.dto.MemberDto;
import com.example.knu_administration_android.encrypt.Encryption;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private EditText loginId, loginPassword;
    private Button loginBtn;
    private TextView signUp;
    private String enterId, enterPassword;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;

    //로그인 프래그먼트 관련 변수는 로그인 프래그먼트에서 처리하는 게 좋습니다
    private static Boolean isLoggedIn=false;

    public void setLoggedIn(boolean isLoggedIn)
    {
        this.isLoggedIn=isLoggedIn;
    }
    public boolean getIsLoggedIn(){
        return isLoggedIn;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_login = inflater.inflate(R.layout.fragment_login, container, false);
        loginId = (EditText) view_login.findViewById(R.id.login_id);
        loginPassword = (EditText) view_login.findViewById(R.id.login_password);
        loginBtn = (Button) view_login.findViewById(R.id.login_btn);
        signUp = (TextView) view_login.findViewById(R.id.signUp);
        hideBottomNavigation(true);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment signUpFragment = new SignUpFragment();
                FragmentManager singInFragmentManager = requireActivity().getSupportFragmentManager();
                singInFragmentManager.popBackStack();
                singInFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, signUpFragment)
                        .addToBackStack("SignUpFragment")
                        .commit();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enterId = loginId.getText().toString();
                enterPassword = loginPassword.getText().toString();
                LoginResponse(enterId, enterPassword);
            }
        });
        return view_login;
    }

    public void LoginResponse(String id, String password){
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();

        Encryption encryption = new Encryption();
        retrofitAPI.userLogin(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Log.d("retrofit", "Data fetch success");
                    String encodePassword = response.body();
                    Log.e("ssdgdsgs", encodePassword);


                    // Base 64 Decode
                    String decodePassword = encryption.Decoding(encodePassword);
                    Log.e("sssssss", decodePassword);

                    // XOR 연산
                    decodePassword = encryption.XORoperator(decodePassword);
                    Log.e("sssssss", decodePassword);

                    if(decodePassword.equals(password))
                    {
                        LoginSuccessGetMemberDto(id);
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "비밀번호를 다시 확인해주십시오", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "아이디를 다시 확인해주십시오", Toast.LENGTH_SHORT).show();
                    setLoggedIn(false);
                }

            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("LoginRetrofit","Data fetch fail1");
                Log.d("retrofit",t.toString());
                setLoggedIn(false);

            }
        });
    }

    public void LoginSuccessGetMemberDto(String accountId){
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.getMember(accountId).enqueue(new Callback<MemberDto>() {
            @Override
            public void onResponse(Call<MemberDto> call, Response<MemberDto> response) {
                if(response.isSuccessful())
                {
                    MemberDto member=response.body();
                    UserPreferences.saveUserInfo(getContext(),member);
                    Toast.makeText(getActivity().getApplicationContext(), member.getName() + "님 환영합니다!", Toast.LENGTH_SHORT).show();
                    setLoggedIn(true);
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    setLoggedIn(false);
                }
            }

            @Override
            public void onFailure(Call<MemberDto> call, Throwable t) {
                Log.d("retrofit","Data fetch fail");
                Log.d("retrofit",t.toString());
                setLoggedIn(false);
            }
        });


    }
    public void hideBottomNavigation(Boolean bool) {
        NavigationBarView navigationBarView = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool)
            navigationBarView.setVisibility(View.GONE);
        else
            navigationBarView.setVisibility(View.VISIBLE);
    }


}