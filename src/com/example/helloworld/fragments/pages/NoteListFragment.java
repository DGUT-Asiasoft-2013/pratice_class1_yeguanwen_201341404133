package com.example.helloworld.fragments.pages;

import java.io.IOException;
import java.util.List;

import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.Article;
import com.example.helloworld.api.entity.Comment;
import com.example.helloworld.api.entity.Page;
import com.example.helloworld.api.entity.User;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NoteListFragment extends Fragment {

        List<Comment> data, data2;
        int page = 0, page2 = 0;
        View view, btnLoadMore, btnLoadMore2;
        ListView listView, listView2;
        TextView textLoadMore, textLoadMore2;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                if (view == null) {
                        view = inflater.inflate(R.layout.fragment_page_note_list, null);
                        
                        btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
                        textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);

                        listView = (ListView) view.findViewById(R.id.comment_list);
                        listView.addFooterView(btnLoadMore);
                        listView.setAdapter(listAdapter);
                        

                        btnLoadMore2 = inflater.inflate(R.layout.widget_load_more_button, null);
                        textLoadMore2 = (TextView) btnLoadMore2.findViewById(R.id.text);

                        listView2 = (ListView) view.findViewById(R.id.mycomment_list);
                        listView2.addFooterView(btnLoadMore2);
                        listView2.setAdapter(listAdapter2);

                        btnLoadMore.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                        loadMoreComment();
                                }
                        });
                        
                        btnLoadMore2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                        loadMoreMyComment();
                                }
                        });

                }
                return view;
        }

        //收到的评论
        BaseAdapter listAdapter = new BaseAdapter() {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                        View view = null;

                        if (convertView == null) {
                                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                                view = inflater.inflate(R.layout.fragment_note_list_item, null);
                        } else {
                                view = convertView;
                        }

                        TextView articleTitle = (TextView) view.findViewById(R.id.tv_article_title);
                        TextView articleEditTime = (TextView) view.findViewById(R.id.article_editTime);
                        AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);
                        TextView commentAuthor = (TextView) view.findViewById(R.id.tv_comment_author);
                        TextView commentContent = (TextView) view.findViewById(R.id.tv_comment_content);
                        TextView commentEditTime = (TextView) view.findViewById(R.id.comment_editTime);

                        Comment comment = data.get(position);

                        articleTitle.setText("Article:" + comment.getArticleTitle());
                        String dateStr = DateFormat.format("yyyy-MM-dd", comment.getArticleEditTime()).toString();
                        articleEditTime.setText(dateStr);

                        commentAuthor.setText(comment.getAuthorName() + ":");
                        commentContent.setText(comment.getText());
                        avatar.load(Server.serverAddress + comment.getAuthorAvatar());
                        String dateStr2 = DateFormat.format("MM-dd hh:mm", comment.getEditDate()).toString();
                        commentEditTime.setText(dateStr2);

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

        //发出的评论
        BaseAdapter listAdapter2 = new BaseAdapter() {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                        View view = null;

                        if (convertView == null) {
                                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                                view = inflater.inflate(R.layout.fragment_note_list_item, null);
                        } else {
                                view = convertView;
                        }

                        TextView articleTitle = (TextView) view.findViewById(R.id.tv_article_title);
                        TextView articleEditTime = (TextView) view.findViewById(R.id.article_editTime);
                        AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);
                        TextView commentAuthor = (TextView) view.findViewById(R.id.tv_comment_author);
                        TextView commentContent = (TextView) view.findViewById(R.id.tv_comment_content);
                        TextView commentEditTime = (TextView) view.findViewById(R.id.comment_editTime);

                        Comment comment = data2.get(position);

                        articleTitle.setText("Article:" + comment.getArticleTitle());
                        String dateStr = DateFormat.format("yyyy-MM-dd", comment.getArticleEditTime()).toString();
                        articleEditTime.setText(dateStr);

                        commentAuthor.setText(comment.getAuthorName() + ":");
                        commentContent.setText(comment.getText());
                        avatar.load(Server.serverAddress + comment.getAuthorAvatar());
                        String dateStr2 = DateFormat.format("MM-dd hh:mm", comment.getEditDate()).toString();
                        commentEditTime.setText(dateStr2);

                        return view;
                }

                @Override
                public long getItemId(int position) {
                        return position;
                }

                @Override
                public Object getItem(int position) {
                        return data2.get(position);
                }

                @Override
                public int getCount() {
                        return data2 == null ? 0 : data2.size();
                }
        };
        
        
        @Override
        public void onResume() {
                super.onResume();
                reloadComment();
                reloadMyComment();
        }

        //获取收到的评论
        void reloadComment() {
                Request request = Server.requestBuilderWithApi("/comments").get().build();
                Server.getSharedClient().newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(Call arg0, Response arg1) throws IOException {
                                try {
                                        // 获取数据
                                        final Page<Comment> data = new ObjectMapper().readValue(arg1.body().string(),
                                                        new TypeReference<Page<Comment>>() {
                                                        });

                                        getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                        NoteListFragment.this.page = data.getNumber();
                                                        NoteListFragment.this.data = data.getContent();
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
        
        //获取发出的评论
        void reloadMyComment() {
                Request request = Server.requestBuilderWithApi("/mycomments").get().build();
                Server.getSharedClient().newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(Call arg0, Response arg1) throws IOException {
                                try {
                                        // 获取数据
                                        final Page<Comment> data = new ObjectMapper().readValue(arg1.body().string(),
                                                        new TypeReference<Page<Comment>>() {
                                                        });

                                        getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                        NoteListFragment.this.page2 = data.getNumber();
                                                        NoteListFragment.this.data2 = data.getContent();
                                                        listAdapter2.notifyDataSetInvalidated();
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
        

        void loadMoreComment() {
                btnLoadMore.setEnabled(false);
                textLoadMore.setText("载入中...");

                Request request = Server.requestBuilderWithApi("comments/" + (page + 1)).get().build();
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
                                        final Page<Comment> notes = new ObjectMapper().readValue(arg1.body().string(),
                                                        new TypeReference<Page<Comment>>() {
                                                        });
                                        if (notes.getNumber() > page) {

                                                getActivity().runOnUiThread(new Runnable() {
                                                        public void run() {
                                                                if (data == null) {
                                                                        data = notes.getContent();
                                                                } else {
                                                                        data.addAll(notes.getContent());
                                                                }
                                                                page = notes.getNumber();
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
        
        void loadMoreMyComment() {
                btnLoadMore2.setEnabled(false);
                textLoadMore2.setText("载入中...");

                Request request = Server.requestBuilderWithApi("mycomments/" + (page + 1)).get().build();
                Server.getSharedClient().newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(Call arg0, Response arg1) throws IOException {
                                getActivity().runOnUiThread(new Runnable() {

                                        public void run() {
                                                btnLoadMore2.setEnabled(true);
                                                textLoadMore2.setText("加载更多");
                                        }
                                });
                                try {
                                        final Page<Comment> notes = new ObjectMapper().readValue(arg1.body().string(),
                                                        new TypeReference<Page<Comment>>() {
                                                        });
                                        if (notes.getNumber() > page2) {

                                                getActivity().runOnUiThread(new Runnable() {
                                                        public void run() {
                                                                if (data2 == null) {
                                                                        data2 = notes.getContent();
                                                                } else {
                                                                        data2.addAll(notes.getContent());
                                                                }
                                                                page2 = notes.getNumber();
                                                                listAdapter2.notifyDataSetChanged();
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
                                                btnLoadMore2.setEnabled(true);
                                                textLoadMore2.setText("加载更多");
                                        }
                                });
                        }
                });
        }
        
}
