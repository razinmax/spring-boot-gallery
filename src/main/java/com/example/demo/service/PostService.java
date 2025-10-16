package com.example.demo.service;

import com.example.demo.model.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostService {
    private final List<Post> posts = new ArrayList<>();

    {
        posts.add(new Post("Does anyone see these posts?", new Date(System.currentTimeMillis()-2*86400000L)));
        posts.add(new Post("Wassup everybody?", new Date(System.currentTimeMillis()-86400000L)));
        posts.add(new Post("Hello world!",new Date()));
    }

    public List<Post> listAllPosts() {
        return posts;
    }

    public void create(String text) {
        posts.add(new Post(text, new Date()));
    }
}
