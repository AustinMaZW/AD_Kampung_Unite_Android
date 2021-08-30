package com.example.ad_project_kampung_unite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ad_project_kampung_unite.data.remote.UserDetailService;
import com.example.ad_project_kampung_unite.entities.UserDetail;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText
            registerUsername,
            registerPassword,
            registerPassword2,
            registerFirstName,
            registerLastName,
            registerPhoneNumber,
            registerAddress;

    Button registerCreateButton;

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerUsername = findViewById(R.id.registerUsername);
        registerPassword = findViewById(R.id.registerPassword);
        registerPassword2 = findViewById(R.id.registerPassword2);
        registerFirstName = findViewById(R.id.registerFirstName);
        registerLastName = findViewById(R.id.registerLastName);
        registerPhoneNumber = findViewById(R.id.registerPhoneNumber);
        registerAddress = findViewById(R.id.registerAddress);

        registerCreateButton = findViewById(R.id.registerCreateButton);
        registerCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetail userDetail = new UserDetail();
                String pwd = registerPassword.getText().toString();
                String pwd2 = registerPassword2.getText().toString();
                userDetail.setUsername(registerUsername.getText().toString());
                userDetail.setPassword(registerPassword.getText().toString());
                userDetail.setFirstName(registerFirstName.getText().toString());
                userDetail.setLastName(registerLastName.getText().toString());
                userDetail.setPhoneNumber(registerPhoneNumber.getText().toString());
                userDetail.setHomeAddress(registerAddress.getText().toString());

                if(checkFormCompletion(userDetail)){
                    String url = getResources().getString(R.string.user_base_url);
                    httpClient.addInterceptor(logging);
                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build());
                    Retrofit retrofit = builder.build();
                    UserDetailService userDetailService = retrofit.create(UserDetailService.class);
                    Call<UserDetail> call = userDetailService.create(userDetail);
                    call.enqueue(new Callback<UserDetail>() {
                        @Override
                        public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                            if (response.isSuccessful()){
                                if(response.body().getId() != 0){
                                    Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                    Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(login);
                                }
                                if(response.body().getId() == 0){
                                    Toast.makeText(RegisterActivity.this, "Username "+ registerUsername.getText().toString() +" already Exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDetail> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private boolean pwdConfirm() {
        boolean confirmed = false;
        String pwd = registerPassword.getText().toString();
        String pwd2 = registerPassword2.getText().toString();
        if(pwd.matches(pwd2)){
            if (!pwd.matches("") && !pwd2.matches("")) {
                confirmed = true;
            }
        }
        return confirmed;
    }

    private boolean checkFormCompletion(UserDetail userDetail) {
        boolean isComplete;
        boolean[] completion = new boolean[]{
                userDetail.getUsername().matches(""),
                !pwdConfirm(),
                userDetail.getFirstName().matches(""),
                userDetail.getLastName().matches(""),
                userDetail.getPhoneNumber().matches(""),
                userDetail.getHomeAddress().matches("")
        };
        String toastString = "Please enter: \n";
        int toastStringLength = toastString.length();
        String[] toastMessages = new String[]{"username", "valid password", "first name", "last name", "phone number", "address"};

        for (int i = 0; i < completion.length; i++) {
            if (completion[i]){
                toastString += toastMessages[i]+"\n";
            }
        }
        if (toastString.length() > toastStringLength){
            Toast.makeText(RegisterActivity.this, toastString, Toast.LENGTH_SHORT).show();
            isComplete = false;
        }else
            isComplete = true;
        return isComplete;
    }

}