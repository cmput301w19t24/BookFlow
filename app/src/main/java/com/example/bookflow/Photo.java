package com.example.bookflow;

import android.net.Uri;

public abstract class Photo {
    private Uri imageURL;
    public Photo (Uri imageURL) {
        this.imageURL = imageURL;
    }
    public Uri getURL () {
        return this.imageURL;
    }
}
