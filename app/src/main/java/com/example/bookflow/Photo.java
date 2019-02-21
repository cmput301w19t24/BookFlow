package com.example.bookflow;

import com.google.firebase.storage;

public abstract class Photo {
    private StorageReference storageReference;
    public Photo (StorageReference storageReference) {
        this.storageReference = storageReference;
    }
    public StorageReference getReference () {
        return this.storageReference;
    }


}
