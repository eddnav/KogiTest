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
    private long createdTime;

    public Caption getCaption() {
        return caption;
    }

    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<ImageResolution, Image> getImages() {
        return images;
    }

    public void setImages(Map<ImageResolution, Image> images) {
        this.images = images;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
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
