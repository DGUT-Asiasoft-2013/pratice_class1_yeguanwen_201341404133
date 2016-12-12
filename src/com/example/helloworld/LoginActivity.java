package com.example.helloworld;

import java.io.IOException;

import org.w3c.dom.UserDataHandler;

import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.User;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity {

        SimpleTextInputCellFragment fragInputCellAccount;
        SimpleTextInputCellFragment fragInputCellPassword;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                fragInputCellAccount = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.input_account);
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

        void goLogin() {
                String account = fragInputCellAccount.getText();
                String password = fragInputCellPassword.getText();

                if (account.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "请正确输入", 0).show();
                        //// new
                        //// AlertDialog.Builder(LoginActivity.this).setMessage("请正确输入").setNegativeButton("OK",
                        //// null).show();
                        return;
                }

                password = MD5.getMD5(password);

                OkHttpClient client = Server.getSharedClient();

                MultipartBody.Builder requestBody = new MultipartBody.Builder().addFormDataPart("account", account)
                                .addFormDataPart("passwordHash", password);

                Request request = Server.requestBuilderWithApi("login").method("post", null).post(requestBody.build())
                                .build();

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("正在登陆，请稍候");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(final Call arg0, final Response arg1) throws IOException {
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                progressDialog.dismiss();
                                        }
                                });
                                try {
                                        ObjectMapper mapper = new ObjectMapper();
                                        final User user = mapper.readValue(arg1.body().string(), User.class);
                                        runOnUiThread(new Runnable() {
                                                public void run() {
                                                        LoginActivity.this.onResponse(arg0, user.getAccount());
                                                }
                                        });
                                } catch (final Exception e) {
                                        runOnUiThread(new Runnable() {
                                                public void run() {
                                                        LoginActivity.this.onFailure(arg0, e);
                                                }
                                        });
                                }

                        }

                        @Override
                        public void onFailure(final Call arg0, final IOException arg1) {
                                progressDialog.dismiss();
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                LoginActivity.this.onFailure(arg0, arg1);
                                        }
                                });

                        }
                });
        }
        // Intent itnt = new Intent(this, HelloWorldActivity.class);
        // startActivity(itnt);

        void onResponse(Call arg0, String user) {
                new AlertDialog.Builder(LoginActivity.this).setTitle("登陆成功").setMessage("Hello, " + user)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                Intent itnt = new Intent(LoginActivity.this, HelloWorldActivity.class);
                                                startActivity(itnt);
                                                finish();
                                        }
                                }).show();
        }

        void onFailure(Call arg0, Exception arg1) {
                new AlertDialog.Builder(LoginActivity.this).setTitle("登录失败").setMessage(arg1.getLocalizedMessage())
                                .setNegativeButton("OK", null).show();
        }

}
