package com.example.helloworld;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.zip.Inflater;

import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.Article;
import com.example.helloworld.api.entity.Comment;
import com.example.helloworld.api.entity.Page;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import okhttp3.Request;
import okhttp3.Response;

public class FeedContentActivity extends Activity {
        TextView article_title, article_content, article_author;
        String title, content, author;
        Article article;
        ListView listView;

        View btnLoadMore;
        TextView textLoadMore;

        List<Comment> data;
        int page = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_feed_content);

                article = (Article) getIntent().getSerializableExtra("article");

                article_title = (TextView) findViewById(R.id.article_title);
                article_title.setText(article.getTitle());
                article_author = (TextView) findViewById(R.id.article_author);
                article_author.setText("author:" + article.getAuthorName());
                article_content = (TextView) findViewById(R.id.article_content);
                article_content.setText(article.getText());

                // Activity里获取布局layout
                LayoutInflater layout = this.getLayoutInflater();
                btnLoadMore = layout.inflate(R.layout.widget_load_more_button, null);
                textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);

                listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(listAdapter);
                // 在底部添加控件
                listView.addFooterView(btnLoadMore);

                findViewById(R.id.send_comment).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                sendComment();
                        }
                });

                btnLoadMore.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                loadMore();
                        }
                });

        }

        void loadMore() {
                btnLoadMore.setEnabled(false);
                textLoadMore.setText("载入中...");

                Request request = Server.requestBuilderWithApi("article/" + article.getid() + "/comments/" + (page + 1))
                                .get().build();
                Server.getSharedClient().newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(Call arg0, Response arg1) throws IOException {
                                FeedContentActivity.this.runOnUiThread(new Runnable() {

                                        public void run() {
                                                btnLoadMore.setEnabled(true);
                                                textLoadMore.setText("加载更多");
                                        }
                                });
                                try {
                                        final Page<Comment> comments = new ObjectMapper().readValue(arg1.body().string(),
                                                        new TypeReference<Page<Comment>>() {
                                                        });
                                        if (comments.getNumber() > page) {

                                                FeedContentActivity.this.runOnUiThread(new Runnable() {
                                                        public void run() {
                                                                if (data == null) {
                                                                        data = comments.getContent();
                                                                } else {
                                                                        data.addAll(comments.getContent());
                                                                }
                                                                page = comments.getNumber();
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
                                FeedContentActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                                btnLoadMore.setEnabled(true);
                                                textLoadMore.setText("加载更多");
                                        }
                                });
                        }
                });
        }

        // 跳转到发送comment页面
        void sendComment() {
                Intent itnt = new Intent(FeedContentActivity.this, SendCommentActivity.class);
                itnt.putExtra("article", article);
                startActivity(itnt);
        }

        // 评论列表
        BaseAdapter listAdapter = new BaseAdapter() {

                @SuppressLint("InflateParams")
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                        View view = null;

                        if (convertView == null) {
                                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                                view = inflater.inflate(R.layout.fragment_list_item, null);
                        } else {
                                view = convertView;
                        }

                        TextView textAuthor = (TextView) view.findViewById(R.id.tv_list_title);
                        TextView textComment = (TextView) view.findViewById(R.id.tv_list_content);
                        TextView textDate = (TextView) view.findViewById(R.id.editTime);
                        AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);

                        Comment comment = data.get(position);

                        textAuthor.setText(comment.getAuthorName());
                        textComment.setText(comment.getText());

                        avatar.load(Server.serverAddress + article.getAuthorAvatar());

                        String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", comment.getCreateDate()).toString();
                        textDate.setText(dateStr);

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

        @Override
        protected void onResume() {
                super.onResume();
                reload();
        }

        void reload() {
                Request request = Server.requestBuilderWithApi("article/" + article.getid() + "/comments").get()
                                .build();
                Server.getSharedClient().newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(Call arg0, Response arg1) throws IOException {
                                try {
                                        // 获取数据
                                        final Page<Comment> data = new ObjectMapper().readValue(arg1.body().string(),
                                                        new TypeReference<Page<Comment>>() {
                                                        });

                                        runOnUiThread(new Runnable() {
                                                public void run() {
                                                        FeedContentActivity.this.page = data.getNumber();
                                                        FeedContentActivity.this.data = data.getContent();
                                                        listAdapter.notifyDataSetInvalidated();
                                                }
                                        });
                                } catch (final Exception e) {
                                        runOnUiThread(new Runnable() {
                                                public void run() {
                                                        new AlertDialog.Builder(FeedContentActivity.this)
                                                                        .setMessage(e.getMessage()).show();
                                                }
                                        });
                                }
                        }

                        @Override
                        public void onFailure(Call arg0, final IOException arg1) {
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                new AlertDialog.Builder(FeedContentActivity.this)
                                                                .setMessage(arg1.getMessage()).show();
                                        }
                                });
                        }
                });
        }

}
