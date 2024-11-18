package rest.controllers;

import DTO.request.DeletePostDto;
import DTO.response.PostResponseDto;
import exceptions.NotUserPostException;
import exceptions.PostDoesNotExistException;
import models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rest.services.CommentContainerService;
import rest.services.HeartContainerService;
import rest.services.PostContainerService;
import rest.services.PostService;

import java.util.List;

@RestController
public class PostController {
    private final PostContainerService postContainerService;
    private final PostService postService;

    @Autowired
    public PostController (PostContainerService postContainerService, PostService postService) {
        this.postContainerService = postContainerService;
        this.postService = postService;
    }

    @GetMapping(value = "/post")
    public List<PostResponseDto> getAllPosts() {
        return postContainerService.getAllPosts();
    }

    @GetMapping(value = "/post/account/{id}")
    public List<Post> getUserPosts(@PathVariable("id") int id) {
        return postContainerService.getUserPosts(id);
    }

    @PostMapping(value = "/post", headers = "Accept=application/json")
    public ResponseEntity<?> createPost(@RequestBody Post post, UriComponentsBuilder ucBuilder) {
        HttpHeaders headers = new HttpHeaders();
        try {
            postContainerService.addUserPost(post);
            headers.setLocation(ucBuilder.path("/post/{id}").buildAndExpand(post.getId()).toUri());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(post, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/post", headers = "Accept=application/json")
    public ResponseEntity<?> updateUserPost(@RequestBody Post post) {
        try {
            postService.updateUserPost(post);
        } catch (PostDoesNotExistException e) {
            return new ResponseEntity<>("This post does not exist!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/post")
    public ResponseEntity<?> deleteUserPost(@RequestBody DeletePostDto deletePostDto) {
        try {
            postContainerService.deleteUserPost(deletePostDto);
        } catch (NotUserPostException e) {
            return new ResponseEntity<>("Can't update this post!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
