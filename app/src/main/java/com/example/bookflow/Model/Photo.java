package com.example.bookflow.Model;

import android.net.Uri;

import java.net.URL;

public class Photo {
    private URL imageURL;
    public Photo (URL imageURL) {
        this.imageURL = imageURL;
    }
    public URL getURL () {
        return this.imageURL;
    }
}
