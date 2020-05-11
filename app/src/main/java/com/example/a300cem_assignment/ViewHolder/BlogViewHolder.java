package com.example.a300cem_assignment.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a300cem_assignment.R;

public class BlogViewHolder extends RecyclerView.ViewHolder {

    public View view;
    private TextView txt_blog_title, txt_blog_date, txt_blog_user, title_blog_des;
    private ImageView image_blog_image, image_blog_circle_icon;


    public BlogViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;

        txt_blog_title = view.findViewById(R.id.txt_blog_title);
        txt_blog_date = view.findViewById(R.id.txt_blog_date);
        txt_blog_user = view.findViewById(R.id.txt_blog_user);
        title_blog_des = view.findViewById(R.id.title_blog_des);
        image_blog_image = view.findViewById(R.id.image_blog_image);
        image_blog_circle_icon = view.findViewById(R.id.image_blog_circle_icon);


    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTxt_blog_title() {
        return txt_blog_title;
    }

    public void setTxt_blog_title(TextView txt_blog_title) {
        this.txt_blog_title = txt_blog_title;
    }

    public TextView getTxt_blog_date() {
        return txt_blog_date;
    }

    public void setTxt_blog_date(TextView txt_blog_date) {
        this.txt_blog_date = txt_blog_date;
    }

    public TextView getTxt_blog_user() {
        return txt_blog_user;
    }

    public void setTxt_blog_user(TextView txt_blog_user) {
        this.txt_blog_user = txt_blog_user;
    }

    public TextView getTitle_blog_des() {
        return title_blog_des;
    }

    public void setTitle_blog_des(TextView title_blog_des) {
        this.title_blog_des = title_blog_des;
    }

    public ImageView getImage_blog_image() {
        return image_blog_image;
    }

    public void setImage_blog_image(ImageView image_blog_view) {
        this.image_blog_image = image_blog_view;
    }

    public ImageView getImage_blog_circle_icon() {
        return image_blog_circle_icon;
    }

    public void setImage_blog_circle_icon(ImageView image_blog_circle_icon) {
        this.image_blog_circle_icon = image_blog_circle_icon;
    }
}
