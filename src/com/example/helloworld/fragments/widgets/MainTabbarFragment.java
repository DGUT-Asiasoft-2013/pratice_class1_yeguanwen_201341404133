package com.example.helloworld.fragments.widgets;

import com.example.helloworld.R;

import android.R.integer;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainTabbarFragment extends Fragment {

        View btnNew, tabFeeds, tabNotes, tabSearch, tabMe;
        View[] tabs;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_main_tabbar, null);

                btnNew = view.findViewById(R.id.btn_new);
                tabFeeds = view.findViewById(R.id.tab_feeds);
                tabNotes = view.findViewById(R.id.tab_notes);
                tabSearch = view.findViewById(R.id.tab_search);
                tabMe = view.findViewById(R.id.tab_me);

                tabs = new View[] { tabFeeds, tabNotes, tabSearch, tabMe };

                for (final View tab : tabs) {
                        tab.setOnClickListener(new View.OnClickListener() {       //¼àÌýµã»÷ÊÂ¼þ

                                @Override
                                public void onClick(View v) {
                                        onTabClicked(tab);
                                }
                        });
                }

                return view;

        }

        public static interface OnTabSeletedListener {
                void onTabSelected(int index);
        }

        OnTabSeletedListener onTabSeletedListener;

        public void setOnTabSeletedListener(OnTabSeletedListener onTabSeletedListener) {
                this.onTabSeletedListener = onTabSeletedListener;
        }
        
        public void setSelectedItem(int index){
                if(index >= 0 && index< tabs.length){
                        onTabClicked(tabs[index]);
                }
        }

        void onTabClicked(View tab) {
                int selecedIndex = -1;

                for (int i = 0; i < tabs.length; i++) {
                        View otherTab = tabs[i];
                        if (otherTab == tab) {
                                otherTab.setSelected(true);
                                selecedIndex = i;
                        }else{
                                otherTab.setSelected(false);
                        }
                }

                if (onTabSeletedListener != null && selecedIndex>=0) {
                        onTabSeletedListener.onTabSelected(selecedIndex);
                }
        }
        
        //void onTabClicked(View tab){
        //     for(otherTab : tabs){
        //           otherTab.setSelected(otherTab == tab);
        //        }
        //}
}
