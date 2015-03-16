package com.luo.luowu.myguoke;

import android.graphics.Bitmap;

/**
 * Created by luowu on 2015/2/6.
 */
public class ViewPager_img {

    String Img_url;
    String title;
    String content;
    Bitmap picture;

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_url() {
        return Img_url;
    }

    public void setImg_url(String img_url) {
        Img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
