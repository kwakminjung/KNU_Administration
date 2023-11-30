package com.example.knu_administration_android.setting;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.knu_administration_android.PersonAdapter;
import com.example.knu_administration_android.LocalDB.UserPreferences;
import com.example.knu_administration_android.R;
import com.example.knu_administration_android.SettingFragment;
import com.example.knu_administration_android.dto.MemberDto;
import com.example.knu_administration_android.encrypt.Encryption;
import com.example.knu_administration_android.network.RetrofitClient;
import com.example.knu_administration_android.network.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.knu_administration_android.encrypt.Encryption;


public class SettingEditProfileFragment extends Fragment {
    private Context context;
    private TextView requestTextView;
    private EditText editProfileNameEt, editProfileIdEt, editProfilePwEt;
    private AppCompatButton editSendBtn;
    private ImageButton editBackBtn;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitAPI;
    private Boolean isProfileChanged=false;
    private MemberDto newMemberDto;
    private MemberViewModel memberViewModel;
    private Encryption encryption;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberViewModel = new ViewModelProvider(requireActivity()).get(MemberViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        memberViewModel.getMemberDto().observe(this, new Observer<MemberDto>() {
            @Override
            public void onChanged(MemberDto newMemberDto) {
                // 변경된 데이터로 UI 업데이트
                editProfileNameEt.setHint(newMemberDto.getName());
                editProfileIdEt.setHint(newMemberDto.getAccountId());
                editProfilePwEt.setHint(encryption.XORoperator(encryption.Decoding(newMemberDto.getPassword())));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_setting_edit_profile, container, false);

        encryption = new Encryption();

        editBackBtn = root.findViewById(R.id.setting_edit_back_btn);
        editBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        editSendBtn = root.findViewById(R.id.edit_send_btn);
        editProfileNameEt = root.findViewById(R.id.edit_profile_name_et);
        editProfileIdEt = root.findViewById(R.id.edit_profile_id_et);
        editProfilePwEt = root.findViewById(R.id.edit_profile_pw_et);

        MemberDto savedMemberDto = UserPreferences.getUserInfo(getContext());
        editProfileNameEt.setHint(savedMemberDto.getName());
        editProfileIdEt.setHint(savedMemberDto.getAccountId());

        //암호화 풀기
        editProfilePwEt.setHint(encryption.XORoperator(encryption.Decoding(savedMemberDto.getPassword())));


        editSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitClient = RetrofitClient.getInstance();
                retrofitAPI=RetrofitClient.getRetrofitInterface();
                newMemberDto = new MemberDto();
                newMemberDto.setMemberId(savedMemberDto.getMemberId());
                newMemberDto.setName(savedMemberDto.getName());
                newMemberDto.setAccountId(savedMemberDto.getAccountId());
                newMemberDto.setPassword(savedMemberDto.getPassword());
                newMemberDto.setBoardNum(savedMemberDto.getBoardNum());
                memberViewModel.setMemberDto(newMemberDto);
                if(editProfileNameEt.length()!=0&&!editProfileNameEt.equals(savedMemberDto.getName()))
                {
                    newMemberDto.setName(editProfileNameEt.getText().toString());
                    isProfileChanged=true;
                }
                if(editProfileIdEt.length()!=0&&!editProfileIdEt.equals(savedMemberDto.getAccountId()))
                {
                    newMemberDto.setAccountId(editProfileIdEt.getText().toString());
                    isProfileChanged=true;
                }
                if(editProfilePwEt.length()!=0&&!editProfilePwEt.equals(savedMemberDto.getPassword()))
                {
                    String password = encryption.Encoding(editProfilePwEt.getText().toString());
                    //암호화 하기
                    newMemberDto.setPassword(encryption.XORoperator(password));
                    isProfileChanged=true;
                }
                retrofitAPI.updateMember(newMemberDto).enqueue(new Callback<MemberDto>() {
                    @Override
                    public void onResponse(Call<MemberDto> call, Response<MemberDto> response) {
                        if(response.isSuccessful())
                        {
                            Log.e("profile", String.valueOf(isProfileChanged));
                            if(isProfileChanged){
                                savedMemberDto.setName(newMemberDto.getName());
                                savedMemberDto.setAccountId(newMemberDto.getAccountId());
                                savedMemberDto.setPassword(newMemberDto.getPassword());
                                Toast.makeText(getActivity(),"변경 성공",Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();
                            }
                            else{
                                Toast.makeText(getActivity(),"변경 사항이 없습니다",Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();
                            }

                        }
                        else{
                            Log.e("retrofit", String.valueOf(response.code())+" "+String.valueOf(editProfileIdEt.length())+" "+String.valueOf(savedMemberDto.getMemberId()));
                            Toast.makeText(getActivity(),"다시 시도해주세요",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<MemberDto> call, Throwable t) {
                        Log.e("retrofit", "Failed to connect: " + t.getMessage());
                    }
                });
            }
        });
        return root;
    }
}