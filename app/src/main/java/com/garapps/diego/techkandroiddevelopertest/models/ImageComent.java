package com.garapps.diego.techkandroiddevelopertest.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageComent implements Serializable{

    @SerializedName("id")
    private String id;
    @SerializedName("image_id")
    private String image_id;
    @SerializedName("comment")
    private String comment;

    public ImageComent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
