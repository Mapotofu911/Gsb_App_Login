package com.galaxy.gsb_app.Class;

/**
 * Created by Mapotofu on 24/04/2017.
 */

public class NewsSingleton {

    private int id;
    private String title;
    private String content;
    private String author;
    private int PlaceNumber;
    private String date;

    private static volatile NewsSingleton instance  = new NewsSingleton();

    private NewsSingleton() {
        super();
    }

    public static NewsSingleton getInstance(){

        if (NewsSingleton.instance == null) {
            // Le mot-clé synchronized sur ce bloc empêche toute instanciation
            // multiple même par différents "threads".
            // Il est TRES important.
            synchronized(NewsSingleton.class) {
                if (NewsSingleton.instance == null) {
                    NewsSingleton.instance = new NewsSingleton();
                }
            }
        }
        return NewsSingleton.instance;
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
