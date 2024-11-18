package rest.controllers;

import models.Comment;
import models.Heart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rest.services.CommentContainerService;
import rest.services.HeartContainerService;

import java.util.List;

@RestController
public class CommentController {
    private final CommentContainerService commentContainerService;

    @Autowired
    public CommentController(CommentContainerService commentContainerService) {
        this.commentContainerService = commentContainerService;
    }

    @GetMapping(value = "/comment/post/{id}")
    public List<Comment> getPostComments(@PathVariable("id") int id) {
        return commentContainerService.getCommentsByPostId(id);
    }

    @PostMapping(value = "/comment", headers = "Accept=application/json")
    public ResponseEntity<?> createComment(@RequestBody Comment comment, UriComponentsBuilder ucBuilder) {
        HttpHeaders headers = new HttpHeaders();
        try {
            commentContainerService.addComment(comment);
            headers.setLocation(ucBuilder.path("/comment/{id}").buildAndExpand(comment.getId()).toUri());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(comment, headers, HttpStatus.CREATED);
    }

    @Transactional
    @DeleteMapping(value = "/comment")
    public ResponseEntity<?> deleteUserComment(@RequestBody Comment comment) {
        try {
            commentContainerService.deleteComment(comment);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
