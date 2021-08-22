package com.example.ad_project_kampung_unite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ad_project_kampung_unite.entities.UserDetail;
import com.example.ad_project_kampung_unite.service.UserDetailService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText registerUsername,
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
                String pwd = registerPassword.getText().toString();
                String pwd2 = registerPassword2.getText().toString();
                UserDetail userDetail = new UserDetail();
                if (pwd.matches(pwd2)){
                    userDetail.setUsername(registerUsername.getText().toString());
                    userDetail.setPassword(registerPassword.getText().toString());
                    userDetail.setFirstName(registerFirstName.getText().toString());
                    userDetail.setLastName(registerLastName.getText().toString());
                    userDetail.setPhoneNumber(registerPhoneNumber.getText().toString());
                    userDetail.setHomeAddress(registerAddress.getText().toString());
                }

                boolean isComplete = checkFormCompletion(userDetail);

                if(isComplete){
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
                                Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(login);
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

    private boolean checkFormCompletion(UserDetail userDetail) {
        boolean isComplete = true;
        if(userDetail.getUsername()!="" | userDetail.getUsername()!= null){}
        else{
            isComplete = false;
            Toast.makeText(RegisterActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
        }
        if(userDetail.getPassword()!="" | userDetail.getPassword()!=null){}
        else{
            isComplete = false;
            Toast.makeText(RegisterActivity.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        }
        if (userDetail.getFirstName() != "" | userDetail.getFirstName() != null){}
        else{
            isComplete= false;
            Toast.makeText(RegisterActivity.this, "Please enter first name", Toast.LENGTH_SHORT).show();
        }
        if(userDetail.getLastName()!="" | userDetail.getLastName()!= null){}
        else{
            isComplete=false;
            Toast.makeText(RegisterActivity.this, "Please enter last name", Toast.LENGTH_SHORT).show();
        }
        if(userDetail.getPhoneNumber()!="" | userDetail.getPhoneNumber()!= null){}
        else{
            isComplete=false;
            Toast.makeText(RegisterActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        }
        if(userDetail.getHomeAddress()!= "" | userDetail.getHomeAddress()!= null){}
        else{
            Toast.makeText(RegisterActivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
        }
        return isComplete;
    }

}