package com.example.bookflow;
import android.net.Uri;

import com.google.firebase.storage.StorageReference;

public class UserPhoto extends Photo {
    public UserPhoto(Uri imageURL) {
        super(imageURL);
    }
}

