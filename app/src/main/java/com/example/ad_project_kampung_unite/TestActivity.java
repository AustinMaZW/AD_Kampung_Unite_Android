package com.example.ad_project_kampung_unite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {
    PostService postService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        postService = RetrofitClient.createService(PostService.class);
        test();
//        try {
//            test2();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void test() {
        Call<List<Post>> call = postService.getPosts();
        // Synchronous call | execute()
        // Synchronous methods are executed on the main thread
//        List<Post> postList = call.execute().body();

        // Asynchronous call | enqueue(Callback<T>)
        // Retrofit performs and handles the method execution in a separated thread
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (response.isSuccessful()) {
                    List<Post> posts = response.body();
                    Log.d("Success", posts.get(3).getBody().toString());
                    TextView textView = findViewById(R.id.text);
                    textView.setText(posts.get(3).getBody().toString());
                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });

    }

    private void test2() throws IOException {
        System.out.println("run test2");
        Call<List<Comment>> call = postService.getComments(1);
        // Synchronous call | execute()
        // Synchronous methods are executed on the main thread
        // The exception that is thrown when attempts to perform a networking operation on its main thread for applications targeting the Honeycomb SDK or higher
        List<Comment> commentList = call.execute().body();
        commentList.stream().forEach(System.out::println);
    }
}