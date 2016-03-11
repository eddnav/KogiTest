package com.nav.kogi.test.shared.api;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Eduardo Naveda
 */
public interface Api {

    String BASE_URL = "https://api.instagram.com/v1/";

    @GET("media/popular")
    Observable<PostsResponse> getPopularPosts(@Query("client_id") String id);

}
