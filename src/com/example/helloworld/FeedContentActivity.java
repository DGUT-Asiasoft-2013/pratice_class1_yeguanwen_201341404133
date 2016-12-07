package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FeedContentActivity extends Activity {
        TextView tv;
        String title;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_feed_content);
                
                title = getIntent().getStringExtra("text");
                
                tv = (TextView) findViewById(R.id.feed_content_title);
                tv.setText(title);
        }
}
