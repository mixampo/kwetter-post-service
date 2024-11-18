package rest.services.interfaces;

import models.Comment;

import java.util.List;
import java.util.Optional;

public interface ICommentConainerService {
    Comment getCommentById(int id);
    List<Comment> getCommentsByPostId(int id);
    void addComment(Comment comment);
    void deleteComment(Comment comment);
    void deleteCommentsByPostId(int id);
}
