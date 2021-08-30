package com.example.ad_project_kampung_unite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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


public class LoginActivity extends AppCompatActivity {

    EditText editText_username, editText_password;
    SharedPreferences sharedPreferences;
    private String auth = "invalidLogin";

    private static final String LOGIN_CREDENTIALS = "LoginCredentials";
    private static final String KEY_USERID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_AUTHENTICATION = "authentication";

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginBtn = findViewById(R.id.loginButton);
        editText_username = findViewById(R.id.username);
        editText_password = findViewById(R.id.password);
        sharedPreferences = getSharedPreferences(LOGIN_CREDENTIALS, MODE_PRIVATE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginBtn != null) {
                    String username = editText_username.getText().toString();
                    String pwd = editText_password.getText().toString();
                    UserDetail user = new UserDetail(username, pwd);
                    if (!user.getUsername().matches("") | !user.getPassword().matches("")){
                        loginRequest(user);
                    }else
                        Toast.makeText(LoginActivity.this, "Please enter Username / Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent backToMain = new Intent(LoginActivity.this, StartUpActivity.class);
        startActivity(backToMain);
    }

    private void loginRequest(UserDetail user) {
        String url = getResources().getString(R.string.user_base_url);
        httpClient.addInterceptor(logging);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());
        Retrofit retrofit = builder.build();

        UserDetailService userDetailService = retrofit.create(UserDetailService.class);
        Call<UserDetail> call = userDetailService.login(user);
        call.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if (response.isSuccessful()){
                    if(response.body().getAuthentication() != null){
                        auth = response.body().getAuthentication();
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_USERNAME, editText_username.getText().toString());
                    editor.putString(KEY_PASSWORD, editText_password.getText().toString());
                    editor.putInt(KEY_USERID, response.body().getId());
                    editor.putString(KEY_AUTHENTICATION, auth);
                    editor.commit();
//                    System.out.println(auth);
                    if (auth != "invalidLogin"){
                        Toast.makeText(LoginActivity.this, "Successful Login", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Enter Valid Username / Password", Toast.LENGTH_SHORT).show();
                    }
                    sharedPreferences = getSharedPreferences(LOGIN_CREDENTIALS, MODE_PRIVATE);
                    if( sharedPreferences.getString(KEY_AUTHENTICATION,"invalidLogin") != "invalidLogin"){
                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(main);
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Enter Valid Username / Password", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}