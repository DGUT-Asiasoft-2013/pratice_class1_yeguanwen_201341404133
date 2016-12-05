package com.example.helloworld;

import com.example.helloworld.fragments.inputcells.PictureInputCellFragment;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class RegisterActivity extends Activity {
	
	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	PictureInputCellFragment fragInputCellImage;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		fragInputCellAccount = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_account);	
		fragInputCellPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);
		fragInputCellImage = (PictureInputCellFragment)getFragmentManager().findFragmentById(R.id.input_image);
		}

	public void onResume(){
		super.onResume();
		fragInputCellAccount.setLabelText("”√ªß√˚");
		fragInputCellAccount.setHintText("«Î ‰»Î”√ªß√˚");
		fragInputCellPassword.setLabelText("√‹¬Î");
		fragInputCellPassword.setHintText("«Î ‰»Î√‹¬Î");
		fragInputCellPassword.setEditText(true);
		fragInputCellPasswordRepeat.setLabelText("÷ÿ∏¥√‹¬Î");
		fragInputCellPasswordRepeat.setHintText("«Î÷ÿ∏¥ ‰»Î√‹¬Î");
		fragInputCellPasswordRepeat.setEditText(true);
		fragInputCellImage.setLabelText("Õº∆¨");
		fragInputCellImage.setHintText("«Î—°‘ÒÕº∆¨");
	}
}
