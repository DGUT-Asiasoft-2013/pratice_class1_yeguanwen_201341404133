package com.example.helloworld;

import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class LoginActivity extends Activity {

	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		fragInputCellAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager()
				.findFragmentById(R.id.input_password);

		findViewById(R.id.btn_forgot_password).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goForgotPassword();
			}
		});

		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goLogin();
			}
		});

		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goRegister();
			}
		});
	}

	void goForgotPassword() {
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}

	void goLogin() {
		Intent itnt = new Intent(this, HelloWorldActivity.class);
		startActivity(itnt);
	}

	void goRegister() {
		Intent itnt = new Intent(this, RegisterActivity.class);
		startActivity(itnt);
	}

	public void onResume() {
		super.onResume();
		fragInputCellAccount.setLabelText("用户名");
		fragInputCellAccount.setHintText("请输入用户名");
		fragInputCellPassword.setLabelText("密码");
		fragInputCellPassword.setHintText("请输入密码");
	}

}
