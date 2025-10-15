package com.example.demo.service;

import com.example.demo.model.Post;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostService {
    public List<Post> listAllPosts(){
        return List.of(
                new Post("Hello world!",new Date()),
                new Post("Wassup everybody?", new Date(System.currentTimeMillis()-86400000L)),
                new Post("Does anyone see these posts?", new Date(System.currentTimeMillis()-2*86400000L))
        );
    }
}
