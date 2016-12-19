package com.example.helloworld.fragments.pages;

import java.io.IOException;

import com.example.helloworld.BootActivity;
import com.example.helloworld.LoginActivity;
import com.example.helloworld.PasswordChangeActivity;
import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.User;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyProfileFragment extends Fragment {
        View view;
        TextView textView;
        ProgressBar progress;
        AvatarView avatar;
        Button changepw, logOut;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                if (view == null) {
                        view = inflater.inflate(R.layout.fragment_page_my_profile, null);
                        textView = (TextView) view.findViewById(R.id.text);
                        progress = (ProgressBar) view.findViewById(R.id.progress);
                        avatar = (AvatarView) view.findViewById(R.id.avatar);

                        logOut = (Button) view.findViewById(R.id.btn_log_out);
                        changepw = (Button) view.findViewById(R.id.btn_change_password);

                        changepw.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                        goChange();
                                }
                        });

                        logOut.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                        goLogOut();
                                }
                        });
                }

                return view;
        }

        @Override
        public void onResume() {
                super.onResume();

                textView.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);

                OkHttpClient client = Server.getSharedClient();
                Request request = Server.requestBuilderWithApi("me").method("get", null).build();

                client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(final Call arg0, Response arg1) throws IOException {
                                try {
                                        final User user = new ObjectMapper().readValue(arg1.body().bytes(), User.class);
                                        getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                        MyProfileFragment.this.onResponse(arg0, user);
                                                }
                                        });
                                } catch (final Exception e) {
                                        getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                        MyProfileFragment.this.onFailuer(arg0, e);
                                                }
                                        });
                                }
                        }

                        @Override
                        public void onFailure(final Call arg0, final IOException arg1) {
                                getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                                MyProfileFragment.this.onFailuer(arg0, arg1);
                                        }
                                });
                        }
                });
        }

        protected void onResponse(Call arg0, User user) {
                progress.setVisibility(View.GONE);
                avatar.load(user);
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(Color.BLACK);
                textView.setText("Hello," + user.getName());
        }

        void onFailuer(Call call, Exception ex) {
                progress.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(Color.RED);
                textView.setText(ex.getMessage());
        }

        void goChange() {
                Intent itnt = new Intent(getActivity(), PasswordChangeActivity.class);
                startActivity(itnt);
        }

        void goLogOut() {
                new AlertDialog.Builder(getActivity()).setTitle("是否注销").setNegativeButton("取消", null)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                Intent itnt = new Intent(getActivity(), LoginActivity.class);
                                                startActivity(itnt);
                                        }
                                }).show();
        }
}
