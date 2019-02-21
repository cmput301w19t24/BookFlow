package com.example.bookflow;

import android.net.Uri;

import java.net.URL;

public abstract class Photo {
    private URL imageURL;
    public Photo (URL imageURL) {
        this.imageURL = imageURL;
    }
    public URL getURL () {
        return this.imageURL;
    }
}
