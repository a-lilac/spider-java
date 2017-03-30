package com.carlos.domain;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;

/**
 * Created by Carlos on 2017/3/23.
 */
@Entity
@Table(name="book2")
@Proxy(lazy = false)
public class Book2 {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "book_name")
    private String bookName;
    @Column(name = "book_price")
    private String bookPrice;
    @Column(name = "book_ID")
    private String bookID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }
}
