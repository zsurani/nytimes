package com.example.zsurani.nytsearch1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zsurani on 6/20/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {


    public ArticleArrayAdapter (Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    static class ViewHolder {
        @BindView(R.id.ivImage) ImageView imageView;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindDrawable(R.drawable.clipart_news) Drawable place;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        Article article = this.getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
            holder = new ViewHolder (convertView);
            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

            //ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
            holder.imageView.setImageResource(0);

            //TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvTitle.setText(article.getHeadline());

            String thumbnail = article.getThumbNail();
            if (!TextUtils.isEmpty(thumbnail)) {
                Glide.with(getContext()).load(thumbnail).placeholder(holder.place).into(holder.imageView);
            }

        return convertView;
    }




}
