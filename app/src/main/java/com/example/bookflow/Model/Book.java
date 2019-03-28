package com.example.bookflow.Model;

/**
 * The book model class. It contains information about a book, including
 * title, author of the book, ISBN number, photoURI, description and book info.
 * It also contains book status information including current status, ownerID
 * and borrower ID.
 */
public class Book {
    private String title;
    private String author;
    private String isbn;
    private float rating;
    private BookStatus status;
    private String ownerId;
    private String borrowerId;
    private int requestCount;
    private String photoUri;
    private String description;
    private String bookId;
    public Book(){

    }

    public Book(String title, String author, String isbn){
        this();

        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.rating = 0;
        this.requestCount = 0;
        this.status = BookStatus.AVAILABLE;
        this.description = "";
    }

    public Book(String title, String author, String isbn, String id){
        this(title, author, isbn);
        this.bookId = id;
    }
    
    /*setter and getter of title*/

    /**
     * get Title
     * @return String
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * set Title
     * @param title String
     */
    public void setTitle(String title){
        this.title = title;
    }
    
    /*setter and getter of author*/

    /**
     * get Author
     * @return String
     */
    public String getAuthor(){
        return this.author;
    }

    /**
     * set author
     * @param author
     */
    public void setAuthor(String author){
        this.author = author;
    }
    
    /*getter and setter of isbn*/

    /**
     * get ISBN
     * @return String
     */
    public String getIsbn(){
        return this.isbn;
    }

    /**
     * set ISBN
     * @param isbn String
     */
    public void setIsbn(String isbn){
        this.isbn = isbn;
    }
    
    /*getter and setter of rating*/

    /**
     * set rating
     * @param rating
     */
    public void setRating(float rating){
        this.rating = rating;
    }

    /**
     * get rating
     * @return the rating
     */
    public float getRating(){
        return this.rating;
    }
    
    /*getter and setter of status*/

    /**
     * set status
     * @param newStatus
     */
    public void setStatus(BookStatus newStatus){
        this.status = newStatus;
    }

    /**
     * get status
     * @return
     */
    public BookStatus getStatus(){
        return this.status;
    }
    
    /*add, get and delete photo*/

    /**
     * set the photo URI
     * @param photo
     */
    public void setPhotoUri(String photo){
        this.photoUri = photo;
    }

    /**
     * get the photo Uri
     * @return
     */
    public String getPhotoUri(){
        return this.photoUri;
    }
    
    public void deletePhotoUri(){
        photoUri = null;
    }
    
    /*getter and setter of owner*/

    /**
     * get owner ID
     * @return
     */
    public String getOwnerId(){
        return this.ownerId;
    }

    /**
     * set owner ID
     * @param ownerId
     */
    public void setOwnerId(String ownerId){
        this.ownerId = ownerId;
    }
    
    /*getter and setter of borrower*/

    /**
     * get borrower ID
     * @return
     */
    public String getBorrowerId(){
        return this.borrowerId;
    }

    /**
     * set borrower ID
     * @param newBorrowerId
     */
    public void setBorrowerId(String newBorrowerId){
        this.borrowerId = newBorrowerId;
    }
    
    /*increase count*/

    /**
     * increate the request counter
     */
    public void countIncrease(){
        this.requestCount ++;
    }
    
    /*getter of count*/

    /**
     * get request counter
     * @return
     */
    public int getCount(){
        return this.requestCount;
    }

    /**
     * get description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * set description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get book ID
     * @return
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * set Book ID
     * @param bookId
     */
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }


    /**
     * get book information
     * @return String
     */
    public String getBookInfo(){
        return isbn + "-" + title +"-" + author + "-" + status;
    }

    /**
     * Book status enumeration
     */
    public enum BookStatus {
        AVAILABLE,
        REQUESTED,
        ACCEPTED,
        BORROWED,
    }
}
