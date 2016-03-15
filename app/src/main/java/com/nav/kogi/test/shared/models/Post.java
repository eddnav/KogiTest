package com.nav.kogi.test.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author Eduardo Naveda
 */
public class Post {

    private Caption caption;
    private List<String> tags;
    private Map<ImageResolution, Image> images;
    private User user;
    private long createdTime;

    public Caption getCaption() {
        return caption;
    }

    public List<String> getTags() {
        return tags;
    }

    public Map<ImageResolution, Image> getImages() {
        return images;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public User getUser() {
        return user;
    }

    public enum ImageResolution {
        @SerializedName("low_resolution")
        LOW,
        @SerializedName("thumbnail")
        THUMBNAIL,
        @SerializedName("standard_resolution")
        STANDARD
    }

}
