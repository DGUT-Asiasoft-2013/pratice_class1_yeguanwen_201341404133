package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;
import com.example.helloworld.fragments.PasswordRecoverStep1Fragment;
import com.example.helloworld.fragments.PasswordRecoverStep1Fragment.OnGoNextListener;
import com.example.helloworld.fragments.PasswordRecoverStep2Fragment;
import com.example.helloworld.fragments.PasswordRecoverStep2Fragment.OnSubmitListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordRecoverActivity extends Activity {
        PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
        PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_password_recover);

                step1.setOnGoNextListener(new OnGoNextListener() {

                        @Override
                        public void onGoNext() {
                                goStep2();
                        }
                });

                step2.setOnSubmitListener(new OnSubmitListener() {

                        @Override
                        public void onSubmit() {
                                goSubmit();
                        }
                });

                getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();
        }

        void goStep2() {
                getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left,
                                                R.animator.slide_in_left, R.animator.slide_out_right)
                                .replace(R.id.container, step2).addToBackStack(null).commit();
        }
        
         void goSubmit() {
                 OkHttpClient client = Server.getSharedClient();
                 
                 MultipartBody.Builder body = new MultipartBody.Builder()
                                 .addFormDataPart("email", step1.getText())
                                 .addFormDataPart("passwordHash", MD5.getMD5(step2.getText()));
                 
                 Request request = Server.requestBuilderWithApi("recover").method("post", null).post(body.build()).build();
                 
                 client.newCall(request).enqueue(new Callback() {
                        
                        @Override
                        public void onResponse(final Call arg0, final Response arg1) throws IOException {
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                try {
                                                        PasswordRecoverActivity.this.onResponse(arg0, arg1);
                                                } catch (Exception e) {
                                                        PasswordRecoverActivity.this.onFailure(arg0, e);
                                                }
                                        }
                                });
                        }
                        
                        @Override
                        public void onFailure(final Call arg0, final IOException arg1) {
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                PasswordRecoverActivity.this.onFailure(arg0, arg1);
                                        }
                                });
                        }
                });
        }
         
         void onResponse(Call arg0, Response arg1){
                 new AlertDialog.Builder(PasswordRecoverActivity.this).setTitle("ÐÞ¸Ä³É¹¦£¬ÇëµÇÂ¼")
                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                                 Intent itnt = new Intent(PasswordRecoverActivity.this, LoginActivity.class);
                                 startActivity(itnt);
                                 finish();
                         }
                 }).show();
         }
         
         void onFailure( Call arg0,  Exception arg1){
                 new AlertDialog.Builder(PasswordRecoverActivity.this)
                 .setTitle("ÐÞ¸ÄÊ§°Ü£¬ÇëÖØÊÔ")
                 .setMessage(arg1.getLocalizedMessage())
                 .setNegativeButton("OK", null)
                 .show();
         }
}
