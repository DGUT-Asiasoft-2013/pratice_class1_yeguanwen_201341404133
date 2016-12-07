package com.example.helloworld;

import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewPassageActivity extends Activity {
        SimpleTextInputCellFragment fragInputCellTitle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_new_passage);
                fragInputCellTitle = (SimpleTextInputCellFragment) getFragmentManager()
                                .findFragmentById(R.id.passage_title);

                findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                finish();
                                overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
                        }
                });

        }

        @Override
        protected void onResume() {
                super.onResume();
                fragInputCellTitle.setLabelText("标题");
                fragInputCellTitle.setHintText("请输入标题");
        }
}
