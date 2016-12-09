package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FeedContentActivity extends Activity {
        TextView tvNav;
        String nav;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_feed_content);
                
                nav = getIntent().getStringExtra("nav");
//                content = getIntent().getStringExtra("content");
                
                tvNav = (TextView) findViewById(R.id.feed_content_nav);
                tvNav.setText(nav);
//                tvContent = (TextView) findViewById(R.id.feed_content_content);
//                tvContent.setText(content);
        }
}
