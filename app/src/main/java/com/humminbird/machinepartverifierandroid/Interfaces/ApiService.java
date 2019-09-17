package com.humminbird.machinepartverifierandroid.Interfaces;

import com.humminbird.machinepartverifierandroid.DataClasses.Person;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("npmv/api/{}")
    Single<Person> getPersonData(@Path("person_id") int personId,
                                 @Query("api_key") String apiKey);
}
