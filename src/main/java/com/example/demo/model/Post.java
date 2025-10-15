package com.example.demo.model;

public class Post {
    private String text;
    private Integer likes;

    public Post(String text){
        this.text = text;
        this.likes = 0;
    }

    public String getText(){
        return text;
    }

    public Integer getLikes(){
        return likes;
    }
}
