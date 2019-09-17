package com.humminbird.machinepartverifierandroid.DataClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person {
    @Expose
    @SerializedName("id")
    private Integer id;
    @Expose
    @SerializedName("imdb_id")
    private String imdbId;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("place_of_birth")
    private String placeOfBirth;

    // bunch of boring getters and setters
}
