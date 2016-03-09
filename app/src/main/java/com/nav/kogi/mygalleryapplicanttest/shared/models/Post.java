package com.nav.kogi.mygalleryapplicanttest.shared.models;

import java.util.List;
import java.util.Map;

/**
 * @author Eduardo Naveda
 */
public class Post {

    private List<String> tags;
    private Map<ImageResolution, Image> images;
    private long createdTime;




    public enum ImageResolution {
        LOW, THUMBNAIL, STANDARD
    }

}
