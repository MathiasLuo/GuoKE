package com.luo.luowu.myguoke;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;


import com.android.volley.toolbox.Volley;

public class PersonalHomePager extends Activity {
	TextView tv_nickname, tv_intro;
	ImageView image_head, net_image;
	Person personal = new Person();
	String user_name, user_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_homepager);
		initView();

		Intent intent = getIntent();
		user_name = intent.getStringExtra("text0");
		user_password = intent.getStringExtra("text1");

		final RequestQueue queue = Volley.newRequestQueue(this);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				"http://apis.guokr.com/community/user/jxn4gy.json", null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						JSONTokener tokener = new JSONTokener(response
								.toString());
						try {
							JSONObject personalHome = (JSONObject) tokener
									.nextValue();
							String nowtime = personalHome.getString("now");
							personal.setTime(nowtime);
							String resource_url= personalHome.getString("resource_url");
							personal.setResource_url(resource_url);
						   JSONObject badge_counts =personalHome.getJSONObject("badge_counts");
							JSONObject PersonalInformation = personalHome
									.getJSONObject("result");
							String nickname = PersonalInformation
									.getString("nickname");
							tv_nickname.setText(nickname);
							personal.setNickname(nickname);
							String city = PersonalInformation.getString("city");
							personal.setCity(city);
							String title = PersonalInformation
									.getString("title");
							personal.setTitle(title);
							String sex = PersonalInformation
									.getString("gender");
							JSONArray taggings = PersonalInformation
									.getJSONArray("taggings");
							JSONObject avatar = PersonalInformation
									.getJSONObject("avatar");
							String large = avatar.getString("large");
							ImageRequest imageRequest = new ImageRequest(large,
									new Listener<Bitmap>() {
										@Override
										public void onResponse(Bitmap response) {
											// TODO Auto-generated method stub
											image_head.setImageBitmap(response);
											personal.setAvatar(response);
										}
									}, 0, 0, Config.ARGB_8888, null);
							queue.add(imageRequest);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(PersonalHomePager.this, "��������02",
								Toast.LENGTH_SHORT).show();
					}
				});
		queue.add(jsonObjectRequest);

	}

	private void initView() {
		tv_intro = (TextView) findViewById(R.id.tv_intro);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		image_head = (ImageView) findViewById(R.id.image_head);
		net_image = (ImageView) findViewById(R.id.net_image);
	}
}
