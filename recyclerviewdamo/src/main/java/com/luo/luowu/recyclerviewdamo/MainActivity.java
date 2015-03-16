package com.luo.luowu.recyclerviewdamo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.recycler,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder( final ViewHolder holder,final int position) {
                      if(position ==10){if (holder.imageView!=null){
                           holder.imageView.setImageResource(R.drawable.guoke);
                          holder.textView.setText("");
                       }}
               else if (position==12 ){
                          if (holder.imageView!=null){
                              holder.imageView.setImageResource(R.drawable.guoke);
                              holder.textView.setText("");
                          }
                       }
                else if(position == 8){
                          if (holder.imageView!=null){
                              holder.imageView.setImageResource(R.drawable.guoke);
                              holder.textView.setText("");
                          }
                       }
                       else if(position == 13){
                          if (holder.imageView!=null){
                              holder.imageView.setImageResource(R.drawable.guoke);
                              holder.textView.setText("");
                          }
                       }
            else
                       {
                           holder.imageView.setImageBitmap(null);
                           holder.textView.setText("hahahahah"+position);
                       }
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
               ImageView imageView;
            TextView textView;
            public ViewHolder(View itemView) {
                super(itemView);
                imageView= (ImageView) itemView.findViewById(R.id.netwrokimageview);
                textView= (TextView) itemView.findViewById(R.id.textview);
            }
        }
    }
}
