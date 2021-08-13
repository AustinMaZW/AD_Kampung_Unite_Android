package com.example.ad_project_kampung_unite.restful;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

// Interface that defines the possible HTTP operations
public interface PostService {

    // The return value wraps the response in a Call object with the type of the expected result
    @GET("posts")
    Call<List<Post>> getPosts();

    // Parameters, added with @Query annotation on a method parameter, are automatically added at the end of the URL
    // comments?postId=1
    @GET("comments")
    Call<List<Comment>> getComments(@Query("postId") Integer id);

    @GET("posts/{id}")
    Call<Post> getPostById(@Path("id") String id);

    // @Body annotation on a method parameter tells Retrofit to use the object as the request body for the call
    @POST("new/post")
    Call<Post> sendPost(@Body Post post);

    @GET("products")
    Call<List<Product>> getProducts();
    @GET("product/{id}")
    Call<Product> changePrice(@Path("id") Integer id);
}
