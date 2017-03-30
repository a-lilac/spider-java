package com.carlos.domain;

/**
 * Created by Carlos on 2017/3/23.
 */
public class BookEntity {
    //书籍实体类
    private String bookID;
    private String bookName;
    private String bookPrice;

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }
}
