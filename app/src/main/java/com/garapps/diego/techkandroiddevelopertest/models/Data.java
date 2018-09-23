package com.garapps.diego.techkandroiddevelopertest.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    @SerializedName("data")
    public List<DataClass> data;
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("tags")
    public List<Tags> tags;


    public class DataClass implements Serializable{

        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("tags")
        public List<Tags> tags;

    }


    /*public class Images implements Serializable{
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("description")
        public String description;
        @SerializedName("link")
        public String link;
    }*/

}
