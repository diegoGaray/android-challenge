package com.garapps.diego.techkandroiddevelopertest.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tags implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    public ArrayList<String> idImages = new ArrayList<>();

    public Tags() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
