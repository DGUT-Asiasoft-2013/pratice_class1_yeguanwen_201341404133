package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.Article;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class SendCommentActivity extends Activity {

        Article article;
        EditText etcomment;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_send_comment);
                article = (Article) getIntent().getSerializableExtra("article");

                etcomment = (EditText) findViewById(R.id.comment_content);
                findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                sendComment();
                        }
                });
        }

        // 发送Comment

        void sendComment() {
                String text = etcomment.getText().toString();
                if (text.isEmpty()) {
                        Toast.makeText(SendCommentActivity.this, "请输入评论", Toast.LENGTH_SHORT).show();
                        return;
                }

                MultipartBody.Builder body = new MultipartBody.Builder().addFormDataPart("text", text);

                Request request = Server.requestBuilderWithApi("article/" + article.getid() + "/comments")
                                .method("post", null).post(body.build()).build();

                Server.getSharedClient().newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(Call arg0, Response arg1) throws IOException {
                                try {
                                        runOnUiThread(new Runnable() {
                                                public void run() {
                                                        new AlertDialog.Builder(SendCommentActivity.this)
                                                                        .setTitle("发送成功").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                        etcomment.setText("");
                                                                                        finish();
                                                                                }
                                                                        })
                                                                        .show();
                                                }
                                        });
                                } catch (Exception e) {
                                        e.printStackTrace();
                                }
                        }

                        @Override
                        public void onFailure(Call arg0, final IOException arg1) {
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                new AlertDialog.Builder(SendCommentActivity.this).setTitle("发送失败")
                                                                .setMessage(arg1.getLocalizedMessage())
                                                                .setPositiveButton("OK", null).show();
                                        }
                                });

                        }
                });
        }
}
