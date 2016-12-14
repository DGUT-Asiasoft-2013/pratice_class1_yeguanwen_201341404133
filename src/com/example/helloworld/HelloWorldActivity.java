package com.example.helloworld;

import com.example.helloworld.fragments.pages.FeedListFragment;
import com.example.helloworld.fragments.pages.MyProfileFragment;
import com.example.helloworld.fragments.pages.NoteListFragment;
import com.example.helloworld.fragments.pages.SearchPageFragment;
import com.example.helloworld.fragments.widgets.MainTabbarFragment;
import com.example.helloworld.fragments.widgets.MainTabbarFragment.OnNewClickedListener;
import com.example.helloworld.fragments.widgets.MainTabbarFragment.OnTabSeletedListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HelloWorldActivity extends Activity {

        FeedListFragment contentFeedList = new FeedListFragment();
        MyProfileFragment contentMyProfile = new MyProfileFragment();
        NoteListFragment contentNoteList = new NoteListFragment();
        SearchPageFragment contentSearchPage = new SearchPageFragment();
        MainTabbarFragment tabbar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_helloworld);

                tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
                tabbar.setOnTabSeletedListener(new OnTabSeletedListener() {

                        @Override
                        public void onTabSelected(int index) {
                                cotentChange(index);
                        }
                });
                
//                findViewById(R.id.btn_new).setOnClickListener(new View.OnClickListener() {
//                        
//                        @Override
//                        public void onClick(View v) {
//                                bringUpEditor();
//                        }
//                });
                
                tabbar.setOnNewClickedListener(new OnNewClickedListener() {
                        
                        @Override
                        public void onNewClicked() {
                                bringUpEditor();
                        }
                });

        }

        @Override
        protected void onResume() {
                super.onResume();
                if(tabbar.getSelectedItem() <0){
                        tabbar.setSelectedItem(0);
                }
        }

        void cotentChange(int index) {
                Fragment newFrag = null;
                switch (index) {
                case 0:
                        newFrag = contentFeedList;
                        break;
                case 1:
                        newFrag = contentNoteList;
                        break;
                case 2:
                        newFrag = contentSearchPage;
                        break;
                case 3:
                        newFrag = contentMyProfile;
                        break;
                default:
                        break;
                }
                if (newFrag == null)
                        return;

                getFragmentManager().beginTransaction().replace(R.id.content, newFrag).commit();
        }

        void bringUpEditor() {
                Intent itnt = new Intent(this, NewArticleActivity.class);
                startActivity(itnt);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);
        }

}
