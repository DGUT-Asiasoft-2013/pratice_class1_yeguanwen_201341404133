package com.example.helloworld;

import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ForgotPassword extends Activity {
	SimpleTextInputCellFragment  fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellEmail;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgotpassword);
		
		fragInputCellAccount = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_account);
		fragInputCellEmail = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_email);
		fragInputCellPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);
		
		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goLogin();
			}
		});
	}
	
	void goLogin(){
		Intent itnt= new Intent(this, LoginActivity.class);
		startActivity(itnt);
	}
	
	public void onResume(){
		super.onResume();
		fragInputCellAccount.setLabelText("�û���");
		fragInputCellAccount.setHintText("�������û���");
		fragInputCellEmail.setLabelText("����");
		fragInputCellEmail.setHintText("����������");
		fragInputCellPassword.setLabelText("������");
		fragInputCellPassword.setHintText("������������");
		fragInputCellPassword.setEditText(true);
		fragInputCellPasswordRepeat.setLabelText("�ظ�������");
		fragInputCellPasswordRepeat.setHintText("���ظ�����������");
		fragInputCellPasswordRepeat.setEditText(true);
	}
}
