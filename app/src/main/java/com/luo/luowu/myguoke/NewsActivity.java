package com.luo.luowu.myguoke;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luowu on 2015/2/6.
 */
public class NewsActivity extends Activity {
    private String CONTENT_URL;
    private String TITLE;
    private RequestQueue queue;
    public Article article = new Article();
    final String GUOKE = ",果壳,果壳网,科技,泛科技,智趣,生活,科普";
    protected ArrayList<String> article_item_arrayList = new ArrayList<String>();
    private MyHandler myHandler;
    private Boolean result;
    private TextView tv_title, tv_avatar, tv_time;
    private RecyclerView recyclerView;

    private Map<String,Bitmap> bitmapMap=new HashMap<String,Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        CONTENT_URL = intent.getStringExtra("url");
        TITLE = intent.getStringExtra("title");
        Log.d("=================>>>>>>>>>>>..", CONTENT_URL);
        Log.d("=================>>>>>>>>>>>..", TITLE);
        myHandler = new MyHandler();
        new NetThread().start();


    }

    public void initView() {
        tv_avatar = (TextView) findViewById(R.id.tv_avatar);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_title = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
    }

    class NetThread extends Thread {
        Document document;

        @Override
        public void run() {
            try {
                document = Jsoup.connect(CONTENT_URL).timeout(10000).get();
                Element element_or = document.select("html").first();
                String or_yes = element_or.select("meta[name],meta[content]").first().attr("content");
                Log.d("++++++++++++++++++============》》》》》》》》》__YES_OR", element_or.select("meta[name],meta[content]").first().attr("content"));
                if (or_yes.equals(GUOKE)) {
                    Element element = document.select("article#contentMain,article.content-main post").first();
                    Log.d("++++++++++++++++++============》》》》》》》》》》》》", element.attr("class"));
                    //解析文章背景
                    Element element_title = element.select("h1.title").first();
                    article.setArticle_title(element_title.text());//文章Title
                    Element element_publish_nickname = element.select("a.author gfl,a[href]").first();
                    Log.d("++++++++++++++++++============》》》》》》》》》》》》", element_publish_nickname.text());
                    article.setAuthor_avatar(element_publish_nickname.text());//文章作者昵称
                    Element element_time = element.select("time.date gfl,time[date_time]").first();
                    Log.d("++++++++++++++++++============》》》》》》》》》》》》", element_time.text());
                    article.setArticle_time(element_time.text());//文章时间
                    //解析文章段落与图片
                    Element element_article = element.select("div#postContent,div.html-text-mixin gbbcode-content").first();
                    Log.d("==============>>>>>>>>>>>>>>>>>>>>", element_article.attr("class"));

                    Elements elements_ps = element_article.select("p");
                    for (int i = 0; i < elements_ps.size(); i++) {
                        String p_text = elements_ps.get(i).text();
                        if (p_text != "") {
                            Log.d("==========.........", p_text);
                            article_item_arrayList.add(p_text);
                        } else {
                            String url_img = elements_ps.get(i).select("noscript img").attr("src");
                            Log.d("==========>>>>>>>>>", url_img);
                            article_item_arrayList.add(url_img);
                            URL url =new URL(url_img);
                            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                            bitmapMap.put(url_img,bitmap);
                            Log.d("=============>>>>>>>>>>>","下载图片成功");
                        }
                    }
                } else {
                    Log.d("==========......", "另一种解析");

                    Element element_title = document.select("div.content-th h1").first();
                    Log.d("==========defsf", element_title.text());
                    article.setArticle_title(element_title.text());//题目
                    Element element_time = document.select("div.content-th-info span").first();
                    Log.d("==========defsf", element_time.text());
                    article.setArticle_time(element_time.text());//时间
                    Element element_publish_nickname = document.select("div.content-th-info a").first();
                    Log.d("===========DEADSD", element_publish_nickname.text());
                    article.setAuthor_avatar(element_publish_nickname.text());//作者昵称
                    Log.d("===========DEADSD", article.getArticle_title());

                    //解析文章段落与图片
                    Element element_article = document.select("div.document").first();
                    Elements elements_ps = element_article.select("p");
                    for (int i = 0; i < elements_ps.size(); i++) {
                        String p_text = elements_ps.get(i).text();
                        Log.d("==========.........", p_text);
                        article_item_arrayList.add(p_text);
                        Element element_img = elements_ps.get(i).select("img").first();
                        try {
                            String string = element_img.attr("src");
                            Log.d("==============>>>>>>>>", string);
                            article_item_arrayList.add(string);

                            URL url =new URL(string);
                            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                            bitmapMap.put(string,bitmap);
                            Log.d("=============>>>>>>>>>>>","下载图片成功");
                        } catch (Exception e) {
                            Log.d("============>>>>", "获取图片和段落出错");
                        }


                    }
                }
                result = true;

            } catch (IOException e) {
                Log.d("++++++++++++++++++============》》》》》》》》》》》》", "Jsoup连接错误");
                e.printStackTrace();
                result = false;
                Toast.makeText(NewsActivity.this, "未知错误。。。", Toast.LENGTH_SHORT).show();
                finish();
            } finally {
                Message message = myHandler.obtainMessage();
                myHandler.sendMessage(message);
            }

        }
    }


    class MyHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("================>>>>>>>>>>>>>>why", article_item_arrayList.size() + "");
            Log.d("============>>>", article.getArticle_time() + article.getArticle_title() + article.getAuthor_avatar());
            setContentView(R.layout.news_activity_ture);
            initView();
            recyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            tv_time.setText(article.getArticle_time());
            tv_title.setText(article.getArticle_title());
            tv_avatar.setText(article.getAuthor_avatar());
            recyclerView.setAdapter(new MyAdapter());
        }
    }

    public void Return(View view) {
        this.finish();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            View itemView;
            TextView textView;
            ImageView networkImageView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                textView = (TextView) itemView.findViewById(R.id.textview);
                networkImageView = (ImageView) itemView.findViewById(R.id.netwrokimageview);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_recyclerview_xml, parent, false);
            ViewHolder myViewHandler = new ViewHolder(v);
            return myViewHandler;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {

            if (article_item_arrayList.get(position).getBytes().length == article_item_arrayList.get(position).length()) {
                TextView textView = holder.textView;
                textView.setText("");
                final ImageView networkImageView = holder.networkImageView;
                networkImageView.setImageBitmap(bitmapMap.get(article_item_arrayList.get(position)));


            } else {
                ImageView networkImageView = holder.networkImageView;
                networkImageView.setImageBitmap(null);
                TextView textView = holder.textView;
                textView.setText(article_item_arrayList.get(position));
                if (article_item_arrayList.get(position).length() < 8) {
                    textView.setTextSize(40);
                    textView.setTextColor(Color.YELLOW);
                } else {
                    textView.setTextSize(20);
                    textView.setTextColor(Color.BLACK);
                }

            }

        }

        @Override
        public int getItemCount() {
            return article_item_arrayList.size();
        }
    }
}






