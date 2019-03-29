package com.example.bookflow;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;

import java.util.ArrayList;
import java.util.List;

public class LinearAdapter extends RecyclerView.Adapter <LinearAdapter.LinearViewHolder>{
    //context
    private Context mContext;
    private List<Book> books;

    public LinearAdapter(Context context ,ArrayList<Book> books){
        this.mContext=context;
        this.books = books;
    }

    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.searchitem,parent,false));
    }


    @Override
    public void onBindViewHolder(LinearAdapter.LinearViewHolder holder, int position) {
       holder.setData(mContext,position);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
    }


    @Override
    public int getItemCount() {
        return 30;
    }


    class LinearViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView isbn;
        private TextView author;
        private ImageView photo;

        public LinearViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.searchDetail1);
            isbn = itemView.findViewById(R.id.searchDetail2);
            author = itemView.findViewById(R.id.searchDetail3);
            photo = itemView.findViewById(R.id.searchItemImage);
        }
        public void setData(Context ctx,int i){
            title.setText(books.get(i).getTitle());
            isbn.setText(books.get(i).getIsbn());
            author.setText(books.get(i).getAuthor());
            Glide.with(ctx).load(books.get(i).getPhotoUri()).into(photo);
        }

    }
}
