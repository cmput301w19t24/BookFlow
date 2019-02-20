package com.example.bookflow;

public interface Owner {
    public void addBook(BookCopy bookCopy);

    public void getBookDescription(String ISBN);

    public void setStatus(BookCopy bookCopy);

    public void viewOwnedBooks();

    public void editBookDescription();

    public void deleteBook();

    public void viewRequest(BookCopy bookCopy);

    public void acceptRequest(BookCopy bookCopy);

    public void declineRequest(BookCopy bookCopy);

    public void handOverBook(BookCopy bookCopy, String ISBN);

    public void receiveReturnedBook(BookCopy bookCopy, String ISBN);

    public void attachPhoto(BookCopy bookCopy);

    public void deletePhoto(BookCopy bookCopy);

    public void specifyGeolocation();
}

