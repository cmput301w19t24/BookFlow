package com.example.bookflow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;

import java.util.ArrayList;

class MyAdapter extends ArrayAdapter<Book> {

    MyAdapter(Context c, ArrayList<Book> books) {
        super(c,R.layout.main_listitem, books);
    }


}
