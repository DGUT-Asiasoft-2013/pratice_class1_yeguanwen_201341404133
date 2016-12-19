package com.example.helloworld.fragments.pages;

import java.io.IOException;
import java.util.List;

import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.Article;
import com.example.helloworld.api.entity.Page;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SearchPageFragment extends Fragment{
        int page=0;
        List<Article> data;
        ImageButton ibnSearch;
        EditText btnkeyword;
        View view, btnLoadMore;
        ListView listView;
        TextView textLoadMore;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                if(view == null){
                view = inflater.inflate(R.layout.fragment_page_search_page, null);
                btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
                textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
                textLoadMore.setText("");
                btnLoadMore.setEnabled(false);
                
                btnkeyword = (EditText) view.findViewById(R.id.search_content);
                ibnSearch = (ImageButton) view.findViewById(R.id.search);
                
                listView = (ListView) view.findViewById(R.id.list);
                listView.setAdapter(listAdapter);
                listView.addFooterView(btnLoadMore);
                
                ibnSearch.setOnClickListener(new View.OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                searchArticle();
                        }
                });
                
                listView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                onItemClicked(position);
                        }
                });
                
                btnLoadMore.setOnClickListener(new View.OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                loadMore();
                        }
                });
                
                }
                return view;
        }
        
        BaseAdapter listAdapter = new BaseAdapter() {
                
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                        View view = null;
                        if(convertView == null){
                                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                                view = inflater.inflate(R.layout.fragment_list_item, null);
                        }else{
                                view = convertView;
                        }
                        //设置内容
                        TextView textTitle = (TextView) view.findViewById(R.id.tv_list_title);
                        TextView textAuthor = (TextView) view.findViewById(R.id.tv_list_author);
                        TextView textContent = (TextView) view.findViewById(R.id.tv_list_content);
                        TextView textDate = (TextView) view.findViewById(R.id.editTime);
                        AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);

                        Article article = data.get(position);
                        textTitle.setText(article.getTitle());
                        textAuthor.setText("Author:" + article.getAuthorName());
                        textContent.setText(article.getText());
                        avatar.load(Server.serverAddress + article.getAuthorAvatar());

                        // 添加时间格式
                        String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
                        textDate.setText(dateStr);
                        
                        textLoadMore.setText("加载更多");
                        btnLoadMore.setEnabled(true);
                        
                        return view;
                }
                
                @Override
                public long getItemId(int position) {
                        return position;
                }
                
                @Override
                public Object getItem(int position) {
                        return data.get(position);
                }
                
                @Override
                public int getCount() {
                        return data == null ? 0 : data.size();
                }
        };
        
        void onItemClicked(int position){
                Article article = data.get(position);
                Intent itnt = new Intent(getActivity(), FeedContentActivity.class);
                itnt.putExtra("article", article);
                startActivity(itnt);
        }
        
        
        void searchArticle() {
                Request request = Server.requestBuilderWithApi("article/s/" + btnkeyword.getText().toString()).get().build();
                
                Server.getSharedClient().newCall(request).enqueue(new Callback() {
                        
                        @Override
                        public void onResponse(Call arg0, Response arg1) throws IOException {
                                try {
                                        final Page<Article> data = new ObjectMapper().readValue(arg1.body().string(),
                                                        new TypeReference<Page<Article>>() {
                                        });
                                        
                                        getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                        SearchPageFragment.this.page = data.getNumber();
                                                        SearchPageFragment.this.data = data.getContent();
                                                        listAdapter.notifyDataSetInvalidated();
                                                }
                                        });
                                } catch (final Exception e) {
                                        getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                        new AlertDialog.Builder(getActivity())
                                                                        .setMessage(e.getMessage()).show();
                                                }
                                        });
                                }
                        }
                        
                        @Override
                        public void onFailure(Call arg0, final IOException arg1) {
                                getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                                new AlertDialog.Builder(getActivity()).setMessage(arg1.getMessage())
                                                                .show();
                                        }
                                });
                        }
                });
        }
        
        void loadMore() {
                btnLoadMore.setEnabled(false);
                textLoadMore.setText("载入中...");
                
                Request request = Server.requestBuilderWithApi("article/s/" + btnkeyword.getText().toString() +"?page="+ (page+1)).get().build();
                Server.getSharedClient().newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(Call arg0, Response arg1) throws IOException {
                                getActivity().runOnUiThread(new Runnable() {

                                        public void run() {
                                                btnLoadMore.setEnabled(true);
                                                textLoadMore.setText("加载更多");
                                        }
                                });
                                try {
                                        final Page<Article> searchs = new ObjectMapper().readValue(arg1.body().string(),
                                                        new TypeReference<Page<Article>>() {
                                                        });
                                        if (searchs.getNumber() > page) {

                                                getActivity().runOnUiThread(new Runnable() {
                                                        public void run() {
                                                                if (data == null) {
                                                                        data = searchs.getContent();
                                                                } else {
                                                                        data.addAll(searchs.getContent());
                                                                }
                                                                page = searchs.getNumber();
                                                                listAdapter.notifyDataSetChanged();
                                                        }
                                                });
                                        }
                                } catch (Exception e) {
                                        e.printStackTrace();
                                }
                        }

                        @Override
                        public void onFailure(Call arg0, IOException arg1) {
                                getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                                btnLoadMore.setEnabled(true);
                                                textLoadMore.setText("加载更多");
                                        }
                                });
                        }
                });
        }
        
}
