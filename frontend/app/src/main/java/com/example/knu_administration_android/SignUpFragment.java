package com.example.knu_administration_android;

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

import com.example.knu_administration_android.dto.MemberDto;
import com.example.knu_administration_android.encrypt.Encryption;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {
    TextView back_login;
    EditText sign_id, sign_password, sign_name;
    Button signUp_bnt;
    String new_id, new_password, new_name;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;
    private LoginFragment loginFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static SignUpFragment newInstance() {
        return  new SignUpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_signup = inflater.inflate(R.layout.fragment_signup, container, false);

        back_login = (TextView) view_signup.findViewById(R.id.back_login);
        sign_id = (EditText) view_signup.findViewById(R.id.sign_id);
        sign_password = (EditText) view_signup.findViewById(R.id.sign_password);
        sign_name = (EditText) view_signup.findViewById(R.id.sign_name);
        signUp_bnt = (Button) view_signup.findViewById(R.id.signUp_btn);
        loginFragment = new LoginFragment();
        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager backLoginFragmentManager = requireActivity().getSupportFragmentManager();
                backLoginFragmentManager.popBackStack();
                backLoginFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, loginFragment)
                        .commit();
            }
        });

        signUp_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_id = sign_id.getText().toString();
                new_password = sign_password.getText().toString();
                new_name = sign_name.getText().toString();
                UserExistCheck(new_id, new_password, new_name);
            }
        });
        return view_signup;
    }

    public void UserExistCheck(String id, String password, String name)
    {
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.checkUserIdExist(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean isUserExist = response.body();
                    // 성공적으로 응답을 받았을 때의 처리
                    if (isUserExist) {
                        // 아이디가 이미 존재하는 경우의 처리
                        Toast.makeText(getActivity().getApplicationContext(), "이미 존재하는 아이디입니다", Toast.LENGTH_SHORT).show();
                    } else {
                        // 아이디가 존재하지 않는 경우의 처리
                        SignUP(id, password, name);
                    }
                } else {
                    // 서버 응답이 실패한 경우의 처리
                    Log.e("retrofit", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("retrofit", "Failed to connect: " + t.getMessage());
            }
        });


    }
    public void SignUP(String id, String password, String name){
        MemberDto memberDto = new MemberDto();
        Encryption encryption = new Encryption();

        memberDto.setAccountId(id);

        // XOR 연산
        String newPassword = encryption.XORoperator(password);
        // Base 64 Encoding
        newPassword = encryption.Encoding(newPassword);
        memberDto.setPassword(newPassword);

        memberDto.setName(name);
        memberDto.setBoardNum(0);
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI=RetrofitClient.getRetrofitInterface();
        retrofitAPI.signUp(memberDto).enqueue(new Callback<MemberDto>() {
            @Override
            public void onResponse(Call<MemberDto> call, Response<MemberDto> response) {
                if(response.isSuccessful())
                {
                    MemberDto createdMember = response.body();
                    Toast.makeText(getActivity().getApplicationContext(), createdMember.getName()+"님 가입을 축하드립니다", Toast.LENGTH_SHORT).show();
                    loginFragment = new LoginFragment();
                    FragmentManager backLoginFragmentManager = requireActivity().getSupportFragmentManager();
                    backLoginFragmentManager.popBackStack();
                    backLoginFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, loginFragment)
                            .addToBackStack("loginFragment")
                            .commit();
                }else {
                    // 회원가입이 실패한 경우의 처리
                    Log.e("retrofit", "Sign up failed. Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MemberDto> call, Throwable t) {
                Log.e("retrofit", "Failed to connect for sign up: " + t.getMessage());
            }
        });

    }

}