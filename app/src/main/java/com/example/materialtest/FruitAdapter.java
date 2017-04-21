package com.example.materialtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder>{

    private static final String TAG = "FruitAdapter";

    private Context mContext;
    public String fruiturl;

    private ArrayList<Bean.ResultBean.DataBean> mFruitList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView fruitImage;
        TextView fruitName;
        TextView newsurl;
        TextView newsdate;
        TextView newsauthor;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            fruitImage = (ImageView) view.findViewById(R.id.news_image);
            fruitName = (TextView) view.findViewById(R.id.news_title);
            newsurl =(TextView)view.findViewById(R.id.news_url);
            newsdate=(TextView)view.findViewById(R.id.news_date);
            newsauthor=(TextView)view.findViewById(R.id.news_author);

        }
    }

    public FruitAdapter(ArrayList<Bean.ResultBean.DataBean> fruitList) {
        mFruitList = fruitList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Bean.ResultBean.DataBean fruit = mFruitList.get(position);
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("title", fruit.getTitle());
                intent.putExtra("thumbnail_pic_s", fruit.getThumbnail_pic_s());
                intent.putExtra("url",fruit.getUrl());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bean.ResultBean.DataBean fruit = mFruitList.get(position);
        holder.fruitName.setText(fruit.getTitle());
        holder.newsurl.setText(fruit.getTitle());
       fruiturl=holder.newsurl.getText().toString();
        Glide.with(mContext).load(fruit.getThumbnail_pic_s()).into(holder.fruitImage);
        holder.newsauthor.setText(fruit.getAuthor_name());
        holder.newsdate.setText(fruit.getDate());
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

}
