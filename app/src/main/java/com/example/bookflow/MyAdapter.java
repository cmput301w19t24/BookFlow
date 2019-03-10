package com.example.bookflow;
import com.example.bookflow.Model.Book;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

class MyAdapter extends ArrayAdapter<Book> {

    public MyAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
