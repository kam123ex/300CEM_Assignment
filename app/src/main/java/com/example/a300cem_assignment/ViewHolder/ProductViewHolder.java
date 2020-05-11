package com.example.a300cem_assignment.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a300cem_assignment.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public View view;
    private TextView row_title, row_price;
    private ImageView row_image, image_favorite;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;

        row_title = view.findViewById(R.id.title_card_view);
        row_price = view.findViewById(R.id.title_card_price);
        row_image = view.findViewById(R.id.image_card_view);
        image_favorite = view.findViewById(R.id.image_favorite);


    }

    public ImageView getImage_favorite() {
        return image_favorite;
    }

    public void setImage_favorite(ImageView image_favorite) {
        this.image_favorite = image_favorite;
    }

    public TextView getRow_title() {
        return row_title;
    }

    public void setRow_title(TextView row_title) {
        this.row_title = row_title;
    }

    public TextView getRow_price() {
        return row_price;
    }

    public void setRow_price(TextView row_price) {
        this.row_price = row_title;
    }

    public ImageView getRow_image() {
        return row_image;
    }

    public void setRow_image(ImageView row_image) {
        this.row_image = row_image;
    }

}
