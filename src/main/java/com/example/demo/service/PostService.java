package com.example.demo.service;

import com.example.demo.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    public List<Post> listAllPosts(){
        return List.of(
                new Post("Hello world!"),
                new Post("Wassup everybody?"),
                new Post("Does anyone see these posts?")
        );
    }
}
