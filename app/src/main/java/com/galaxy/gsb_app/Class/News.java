package com.galaxy.gsb_app.Class;

/**
 * Created by Mapotofu on 21/04/2017.
 */

public class News {

    private int id;
    private String title;
    private String content;
    private int user_id;
    private int PlaceNumber;
    private String date;

    public News(int id, String title, String content, int user_id, int placeNumber, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
