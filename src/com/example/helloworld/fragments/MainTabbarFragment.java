package com.example.helloworld.fragments;

import com.example.helloworld.R;

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
                // TODO Auto-generated method stub
                return view;
                
        }
}
