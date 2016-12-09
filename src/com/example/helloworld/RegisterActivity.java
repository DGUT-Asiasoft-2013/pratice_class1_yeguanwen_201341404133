package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.fragments.inputcells.PictureInputCellFragment;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

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

public class RegisterActivity extends Activity {

        SimpleTextInputCellFragment fragInputCellAccount;
        SimpleTextInputCellFragment fragInputCellName;
        SimpleTextInputCellFragment fragInputCellEmail;
        SimpleTextInputCellFragment fragInputCellPassword;
        SimpleTextInputCellFragment fragInputCellPasswordRepeat;
        PictureInputCellFragment fragInputCellImage;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_register);

                fragInputCellAccount = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.input_account);
                fragInputCellEmail = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.input_email);
                fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.input_password);
                fragInputCellPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.input_password_repeat);
                fragInputCellName = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.input_name);

                fragInputCellImage = (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_image);

                findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                submit();
                                // goLogin();
                        }
                });
        }

        public void onResume() {
                super.onResume();
                fragInputCellAccount.setLabelText("”√ªß√˚");
                fragInputCellAccount.setHintText("«Î ‰»Î”√ªß√˚");
                fragInputCellName.setLabelText("Í«≥∆");
                fragInputCellName.setHintText("«Î ‰»ÎÍ«≥∆");
                fragInputCellEmail.setLabelText("” œ‰");
                fragInputCellEmail.setHintText("«Î ‰»Î” œ‰");
                fragInputCellPassword.setLabelText("√‹¬Î");
                fragInputCellPassword.setHintText("«Î ‰»Î√‹¬Î");
                fragInputCellPassword.setEditText(true);
                fragInputCellPasswordRepeat.setLabelText("÷ÿ∏¥√‹¬Î");
                fragInputCellPasswordRepeat.setHintText("«Î÷ÿ∏¥ ‰»Î√‹¬Î");
                fragInputCellPasswordRepeat.setEditText(true);
                fragInputCellImage.setLabelText("—°‘ÒÕº∆¨");
                fragInputCellImage.setHintText("«Î—°‘ÒÕº∆¨");
        }

        void submit() {
                String password = fragInputCellPassword.getText();
                String passwordRepeat = fragInputCellPasswordRepeat.getText();

                if (!password.equals(passwordRepeat)) {
                        Toast.makeText(RegisterActivity.this, "÷ÿ∏¥√‹¬Î≤ª“ª÷¬", Toast.LENGTH_LONG).show();
                        // new AlertDialog.Builder(this).setMessage("÷ÿ∏¥√‹¬Î≤ª“ª÷¬")
                        // .setNegativeButton("OK", null).show();
                        return;
                }

                String account = fragInputCellAccount.getText();
                String name = fragInputCellName.getText();
                String email = fragInputCellEmail.getText();

                OkHttpClient client = new OkHttpClient();

                
                //ππ‘Ï∑¢ÀÕƒ⁄»›
                MultipartBody.Builder requestBody = new MultipartBody.Builder()
                                .addFormDataPart("account", account)
                                .addFormDataPart("name", name)
                                .addFormDataPart("email", email)
                                .addFormDataPart("passwordHash", password);

                //ππ‘Ï∑¢ÀÕ«Î«Û
                Request request = new Request.Builder()
                                .url("http://172.27.0.59:8080/membercenter/api/register")
                                .method("post", null)
                                .post(requestBody.build())
                                .build();

                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("«Î…‘∫Ú");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(final Call arg0, final Response arg1) throws IOException {
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                try {
                                                        progressDialog.dismiss();
                                                        RegisterActivity.this.onResponse(arg0, arg1.body().string());

                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                        onFailure(arg0, e);
                                                }

                                        }
                                });
                        }

                        @Override
                        public void onFailure(final Call arg0, final IOException arg1) {
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                progressDialog.dismiss();
                                                RegisterActivity.this.onFailure(arg0, arg1);
                                        }
                                });
                        }
                });

        }

        // void goLogin() {
        // Intent itnt = new Intent(this, LoginActivity.class);
        // startActivity(itnt);
        // }

        void onResponse(Call arg0, String responseBody) {
                new AlertDialog.Builder(this).setTitle("◊¢≤·≥…π¶").setMessage(responseBody)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                        }
                                }).show();
        }

        void onFailure(Call arg0, Exception arg1) {
                new AlertDialog.Builder(this).setTitle("◊¢≤· ß∞‹").setMessage(arg1.getLocalizedMessage())
                                .setNegativeButton("OK", null).show();
        }

}
