package com.galaxy.gsb_app.Class;

/**
 * Created by Mapotofu on 21/04/2017.
 */

public class News {

    private int id;
    private String title;
    private String content;
    private String author;
    private int PlaceNumber;
    private String date;

    public News(int id, String title, String content, String author, int placeNumber, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.PlaceNumber = placeNumber;
        this.date = date;
    }

    public News() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPlaceNumber() {
        return PlaceNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.PlaceNumber = placeNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
