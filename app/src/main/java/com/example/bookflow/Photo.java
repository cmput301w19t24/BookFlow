package com.example.bookflow;

public abstract class Photo {
    private StorageReference storageReference;
    public Photo (StorageReference storageReference) {
        this.storageReference = storageReference;
    }
    public StorageReference getReference () {
        return this.storageReference;
    }
}
