package com.example.kubus001;

public class Note {
    private String _id;
    private String title;
    private String text;
    private String color;
    private String path;

    public Note(String _id, String title, String text, String color, String path) {
        this._id = _id;
        this.title = title;
        this.text = text;
        this.color = color;
        this.path = path;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
