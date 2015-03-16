package com.luo.luowu.myguoke;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by luowu on 2015/2/6.
 */
public class Article {
    String article_title;
    ArrayList<String> article_ps;
    ArrayList<Bitmap> bitmaps;
    String author_avatar;
    String article_time;

    public String getArticle_time() {
        return article_time;
    }

    public void setArticle_time(String article_time) {
        this.article_time = article_time;
    }

    public String getAuthor_avatar() {
        return author_avatar;
    }

    public ArrayList<String> getArticle_ps() {
        return article_ps;
    }

    public void setAuthor_avatar(String author_avatar) {
        this.author_avatar = author_avatar;
    }

    public void setArticle_ps(ArrayList<String> article_ps) {
        this.article_ps = article_ps;
    }

    public void setBitmaps(ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public ArrayList<Bitmap> getBitmaps() {
        return bitmaps;

    }

    public String getArticle_title() {

        return article_title;
    }
}
