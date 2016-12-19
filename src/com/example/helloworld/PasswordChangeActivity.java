package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordChangeActivity extends Activity {
        SimpleTextInputCellFragment fragOldPassword, fragNewPassword, fragPasswordRepeat;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_password_change);

                fragOldPassword = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.old_password);
                fragNewPassword = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.new_password);
                fragPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.password_repeat);

                findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                confirm();
                        }
                });
                
                findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                cancel();
                        }
                });
        }

        @Override
        protected void onResume() {
                super.onResume();
                fragOldPassword.setLabelText("æ…√‹¬Î");
                fragOldPassword.setHintText("«Î ‰»Îæ…√‹¬Î");
                fragOldPassword.setEditText(true);
                fragNewPassword.setLabelText("–¬√‹¬Î");
                fragNewPassword.setHintText("«Î ‰»Î–¬√‹¬Î");
                fragNewPassword.setEditText(true);
                fragPasswordRepeat.setLabelText("÷ÿ∏¥√‹¬Î");
                fragPasswordRepeat.setHintText("«Î÷ÿ∏¥ ‰»Î√‹¬Î");
                fragPasswordRepeat.setEditText(true);
        }
        
        void confirm() {
                new AlertDialog.Builder(PasswordChangeActivity.this).setTitle("–ﬁ∏ƒ√‹¬Î").setNegativeButton("»°œ˚", null)
                                .setPositiveButton("»∑»œ", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                change();
                                        }
                                }).show();
        }

        void cancel(){
                finish();
        }
        
        void change() {
                String oldPassword = fragOldPassword.getText();
                String newPassword = fragNewPassword.getText();
                String passwordRepeat = fragPasswordRepeat.getText();
                
                if(! newPassword.equals(passwordRepeat)){
                        Toast.makeText(PasswordChangeActivity.this, "÷ÿ∏¥√‹¬Î≤ª“ª÷¬", Toast.LENGTH_SHORT).show();
                        return;
                }
                
                MultipartBody body = new MultipartBody.Builder()
                                .addFormDataPart("oldPasswordHash", MD5.getMD5(oldPassword))
                                .addFormDataPart("newPasswordHash", MD5.getMD5(newPassword))
                                .build();
                
                Request request = Server.requestBuilderWithApi("change").post(body).build();
                
                Server.getSharedClient().newCall(request).enqueue(new Callback() {
                        
                        @Override
                        public void onResponse(final Call arg0, final Response arg1) throws IOException {
                                try {
                                        final Boolean isChange = new ObjectMapper().readValue(arg1.body().string(), Boolean.class);
                                        runOnUiThread(new Runnable() {
                                                public void run() {
                                                        if(isChange){
                                                                PasswordChangeActivity.this.onResponse(arg0, arg1);
                                                                 }else{
                                                                         Toast.makeText(PasswordChangeActivity.this, "æ…√‹¬Î¥ÌŒÛ", Toast.LENGTH_SHORT);
                                                                 }
                                                }
                                        });
                                       
                                } catch (Exception e) {
                                        PasswordChangeActivity .this.onFailure(arg0, e);
                                }
                        }
                        
                        @Override
                        public void onFailure(Call arg0, IOException arg1) {
                               PasswordChangeActivity .this.onFailure(arg0, arg1);
                        }
                });
        }
        void onResponse(Call arg0, Response arg1){
                new AlertDialog.Builder(PasswordChangeActivity.this).setTitle("–ﬁ∏ƒ≥…π¶£¨«Îµ«¬º")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                Intent itnt = new Intent(PasswordChangeActivity.this, LoginActivity.class);
                                startActivity(itnt);
                                finish();
                        }
                }).show();
        }
        
        void onFailure(Call arg0, Exception arg1) {
                new AlertDialog.Builder(PasswordChangeActivity.this)
                .setTitle("–ﬁ∏ƒ ß∞‹£¨«Î÷ÿ ‘")
                .setMessage(arg1.getLocalizedMessage())
                .setNegativeButton("OK", null)
                .show();
        }
}
