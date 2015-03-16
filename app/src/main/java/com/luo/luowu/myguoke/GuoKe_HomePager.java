package com.luo.luowu.myguoke;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by luowu on 2015/2/6.
 */
public class GuoKe_HomePager extends Activity {

    private ViewPager viewPager;
    private ArrayList<ViewPager_img> list_viewpager_item = new ArrayList<ViewPager_img>();
    private ArrayList<View> list_view = new ArrayList<View>();
    private RequestQueue queue;
    private ImageView img_0, img_1, img_2, img_3, img_4;
    private ImageView img_back;
    private int currindex;
    private TextView title;
    private ArrayList<String> recyclerview_item_title = new ArrayList<String>();
    private ArrayList<String> recyclerview_item_url = new ArrayList<String>();
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置全屏。
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirstHandler handler = new FirstHandler();
        queue = Volley.newRequestQueue(this);
        new Net_Thread(handler).start();

    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        img_0 = (ImageView) findViewById(R.id.img_0);
        img_1 = (ImageView) findViewById(R.id.img_1);
        img_2 = (ImageView) findViewById(R.id.img_2);
        img_3 = (ImageView) findViewById(R.id.img_3);
        img_4 = (ImageView) findViewById(R.id.img_4);
        title = (TextView) findViewById(R.id.action_bar_title);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_0.setAlpha(100);
        img_1.setAlpha(100);
        img_2.setAlpha(100);
        img_3.setAlpha(100);
        img_4.setAlpha(100);
        recyclerView= (RecyclerView) findViewById(R.id.home_recyclerview);

    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list_view.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(list_view.get(position), 0);
            return list_view.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView(list_view.get(position));
        }

    }

    class Net_Thread extends Thread {
        FirstHandler handler;

        public Net_Thread(FirstHandler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Document doc;
            try {
                doc = Jsoup.connect("http://www.guokr.com/").timeout(10000)
                        .get();
                Element element_recyclerview_item = doc.select("ul.titles").first();

                Elements elements_url = element_recyclerview_item.select("li a");
                Elements elements_titles = element_recyclerview_item.select("li a h4");
                for (int i = 0; i < elements_titles.size();i++){
                    String string_url = elements_url.get(i).attr("href");
                    String string_title = elements_titles.get(i).text();
                     recyclerview_item_title.add(string_title);
                    recyclerview_item_url.add(string_url);
                    Log.d("得到的文章题目和url",string_title+string_url);

                }

                Element element = doc.select("section.content-block").first();
                Elements img_elements = element.select("li a img");
                Elements title_elements = element.select("li a h4");
                Elements content_Elements = element.select("li a");
                RequestQueue queue = Volley
                        .newRequestQueue(GuoKe_HomePager.this);
                for (int i = 0; i < img_elements.size(); i++) {

                    String img_url = img_elements.get(i).attr("src");
                    String title = title_elements.get(i).text();
                    String content = content_Elements.get(i).attr("href");
                    Log.d("======================>>>>>", img_url + "   "
                            + title + " ///////////////////////////////  " + content);
                    ViewPager_img viewPager_img = new ViewPager_img();
                    viewPager_img.setContent(content);
                    viewPager_img.setImg_url(img_url);
                    viewPager_img.setTitle(title);
                    list_viewpager_item.add(viewPager_img);
                }
                for (int i1 = 0; i1 < list_viewpager_item.size(); i1++) {

                    ImageRequest imageRequest = new ImageRequest(
                            list_viewpager_item.get(i1).getImg_url(),
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    // TODO Auto-generated method stub
                                    list_view.add(new View_Bitmap(getApplicationContext(), response));
                                    if (list_view.size() == 5) {
                                        Message message = handler.obtainMessage();
                                        handler.sendMessage(message);
                                    }

                                }
                            }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.e("============>>>>>>>>>>>>", "网络连接错误");
                        }
                    });
                    queue.add(imageRequest);
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {

            }

        }
    }

    class FirstHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            setContentView(R.layout.homepager);
            initView();
            img_back.getBackground().setAlpha(100);
            viewPager.setAdapter(new MyAdapter());
            viewPager.setOnPageChangeListener(new pager_listener());
            title.setText(list_viewpager_item.get(currindex).getTitle());
            title.setOnClickListener(new title_listener());
            Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", list_viewpager_item.get(currindex).getTitle());


            recyclerView.setLayoutManager(new LinearLayoutManager(GuoKe_HomePager.this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(new MyHomeAdapter());
        }
    }

    class pager_listener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageSelected(int position) {
            TranslateAnimation annotation = null;
            switch (position) {
                case 0:
                    img_0.setImageResource(R.drawable.point1);
                    img_1.setImageResource(R.drawable.point0);
                    break;
                case 1:
                    img_1.setImageResource(R.drawable.point1);
                    img_0.setImageResource(R.drawable.point0);
                    img_2.setImageResource(R.drawable.point0);
                    break;

                case 2:
                    img_2.setImageResource(R.drawable.point1);
                    img_1.setImageResource(R.drawable.point0);
                    img_3.setImageResource(R.drawable.point0);
                    break;

                case 3:
                    img_3.setImageResource(R.drawable.point1);
                    img_2.setImageResource(R.drawable.point0);
                    img_4.setImageResource(R.drawable.point0);
                    break;

                case 4:
                    img_4.setImageResource(R.drawable.point1);
                    img_3.setImageResource(R.drawable.point0);
                    break;
            }
            currindex = position;
            title.setText(list_viewpager_item.get(position).getTitle());
        }
    }

    class title_listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(GuoKe_HomePager.this, NewsActivity.class);
            intent.putExtra("url", list_viewpager_item.get(currindex).getContent());
            intent.putExtra("title", list_viewpager_item.get(currindex).getTitle());
            startActivity(intent);
        }
    }

    class MyHomeAdapter extends  RecyclerView.Adapter<MyHomeAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recyclerview_item,parent,false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
              holder.textView.setText(recyclerview_item_title.get(position));
            holder.itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itent =new Intent(GuoKe_HomePager.this,NewsActivity.class);
                    itent.putExtra("url",recyclerview_item_url.get(position));
                    itent.putExtra("title",recyclerview_item_title.get(position));
                    startActivity(itent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return recyclerview_item_title.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
                 TextView textView;
            View itemview;
            public ViewHolder(View itemView) {
                super(itemView);
                textView= (TextView) itemView.findViewById(R.id.home_recyclerview_textview);
               this.itemview=itemView;
            }

        }
    }
    public void Loading (View view){

        Intent intent =new Intent(GuoKe_HomePager.this,Login.class);
        startActivity(intent);
    }
}