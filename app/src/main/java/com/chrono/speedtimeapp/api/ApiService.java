package com.chrono.speedtimeapp.api;

import com.chrono.speedtimeapp.model.Time;
import com.chrono.speedtimeapp.model.Track;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("time")
    Call<Void> postData(@Body Time time);

    @GET("track/{idTrack}")
    Call<Track> getTrackById(@Path("idTrack") long idTrack);
}

