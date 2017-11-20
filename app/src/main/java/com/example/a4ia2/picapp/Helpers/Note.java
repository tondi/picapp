package com.example.a4ia2.picapp.Helpers;

/**
 * Created by mtond_000 on 18.11.2017.
 */

public class Note {
    private final int id;
    private String title;
    private String description;
    private String color;
    private String path;

    public Note(int id, String title, String description, String color, String path) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.color = color;
        this.path = path;
    }

    public int getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getPath() {
        return path;
    }


    public void setA(String newTitle) {
        this.title = newTitle;
    }

    public void setDescription (String newDescription) {
        this.description = newDescription;
    }
}
