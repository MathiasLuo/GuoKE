package com.luo.luowu.myguoke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity {
	EditText user_name, user_password;
	String text0, text1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initView();
	}

	protected void initView() {
		user_name = (EditText) findViewById(R.id.user_name);
		user_password = (EditText) findViewById(R.id.user_password);
	}

	public void resume(View v) {
		user_name.setText(null);
		user_password.setText(null);
	}

	public void load(View v) {
		text0 = user_name.getText().toString();
		text1= user_password.getText().toString();
		Intent intent =new Intent(Login.this,PersonalHomePager.class);
		intent.putExtra("text0", text0);
		intent.putExtra("text1", text1);
		startActivity(intent);
	}
}
