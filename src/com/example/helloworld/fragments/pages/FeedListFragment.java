package com.example.helloworld.fragments.pages;

import java.util.Random;
import java.util.zip.Inflater;

import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FeedListFragment extends Fragment {
        View view;
        ListView listView;
        String[] data;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                if (view == null) {
                        view = inflater.inflate(R.layout.fragment_page_feed_list, null);

                        listView = (ListView) view.findViewById(R.id.list);
                        listView.setAdapter(listAdapter);
                        
                        listView.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        onItemClicked(position);
                                }
                        });
                        
                        Random rand = new Random();
                        data = new String[10+ Math.abs(rand.nextInt()%20)];
                        for(int i=0; i<data.length; i++){
                                data[i] = "This row is "  + rand.nextInt();
                        }
                        
                }
                return view;
        }

        BaseAdapter listAdapter = new BaseAdapter() {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                        View view = null;

                        if (convertView == null) {
                                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                                view = inflater.inflate(android.R.layout.simple_list_item_1, null);
                        } else {
                                view = convertView;
                        }

                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        text1.setText(data[position]);
                                                
                        return view;
                }

                @Override
                public long getItemId(int position) {
                        // 取出与列表中与指定索引对应行的ID
                        return position;
                }

                @Override
                public Object getItem(int position) {
                        // 获取数据中与指定索引对应的数据项
                        return data[position];
                }

                @Override
                public int getCount() {
                        // 数据的条目数
                        return data == null ? 0 : data.length;
                }
        };
        
        void onItemClicked(int position){
                String text = data[position];
                Intent itnt = new Intent(getActivity(), FeedContentActivity.class);
                itnt.putExtra("text", text);
                startActivity(itnt);
        }
        
}
