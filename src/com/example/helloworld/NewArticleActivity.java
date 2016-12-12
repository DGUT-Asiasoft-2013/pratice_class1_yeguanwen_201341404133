package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewArticleActivity extends Activity {
        SimpleTextInputCellFragment fragInputCellTitle;
        EditText et_content;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_new_passage);
                fragInputCellTitle = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.article_title);
                
                et_content = (EditText) findViewById(R.id.content);

                findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                sendArticle();
                        }
                });

        }

        @Override
        protected void onResume() {
                super.onResume();
                fragInputCellTitle.setLabelText("标题");
                fragInputCellTitle.setHintText("请输入标题");
        }
        
        void sendArticle() {
               String title =  fragInputCellTitle.getText();
               String text = et_content.getText().toString();
               
               OkHttpClient client = Server.getSharedClient();
               
               MultipartBody.Builder body = new MultipartBody.Builder().addFormDataPart("title", title).addFormDataPart("text", text);
               
               Request request = Server.requestBuilderWithApi("article").method("post", null).post(body.build()).build();
               
               client.newCall(request).enqueue(new Callback() {
                
                @Override
                public void onResponse(Call arg0, Response arg1) throws IOException {
                        try {
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                new AlertDialog.Builder(NewArticleActivity.this)
                                                .setTitle("发送成功")
                                                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                                        
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                                finish();
                                                                overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
                                                        }
                                                }).show();
                                        }
                                });
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
                
                @Override
                public void onFailure(Call arg0, IOException arg1) {
                        
                }
        });
        }
}
