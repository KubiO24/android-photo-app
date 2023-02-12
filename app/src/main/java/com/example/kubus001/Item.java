package com.example.kubus001;

public class Item {
    private String name;
    private String url;
    private String time;
    private String size;

    public Item(String name, String url, String time, String size) {
        this.name = name;
        this.url = url;
        this.time = time;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
