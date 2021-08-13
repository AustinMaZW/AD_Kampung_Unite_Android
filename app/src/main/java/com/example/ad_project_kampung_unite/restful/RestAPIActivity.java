package com.example.ad_project_kampung_unite.restful;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ad_project_kampung_unite.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAPIActivity extends AppCompatActivity {

//    Retrofit retrofit;
    PostService postService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_api);

        postService = RetrofitClient.createService(PostService.class);
        test();
//        try {
//            test2();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void test() {
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd")
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://jsonplaceholder.typicode.com/")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//        PostService postService = retrofit.create(PostService.class);

       Call<List<Post>> call = postService.getPosts();
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
                    Log.e("Error", String.valueOf(response.code()));
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

/*        Call<List<Product>> call2 = postService.getProducts();
        call2.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                if (response.isSuccessful()) {
                    List<Product> comments = response.body();
                    for (Product comment:comments) {
                        System.out.println(comment);
                    }
                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });*/
    }

    private void test2() throws IOException {
        System.out.println("run test2");
        Call<List<Product>> call = postService.getProducts();
        // Synchronous call | execute()
        // Synchronous methods are executed on the main thread
        // The exception 'android.os.NetworkOnMainThreadException' is thrown when an application attempts to perform a networking operation on its main thread.
        // but only thrown for applications targeting the Honeycomb SDK or higher
        List<Product> postList = call.execute().body();
        postList.stream().forEach(System.out::println);
    }
}