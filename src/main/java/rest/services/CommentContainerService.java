package rest.services;

import models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.repositories.ICommentJpaRepository;
import rest.services.interfaces.ICommentConainerService;

import java.util.List;

@Service
public class CommentContainerService implements ICommentConainerService {
    private final ICommentJpaRepository commentJpaRepository;

    @Autowired
    public CommentContainerService(ICommentJpaRepository commentJpaRepository) {
        this.commentJpaRepository = commentJpaRepository;
    }

    @Override
    public Comment getCommentById(int id) {
        return commentJpaRepository.getOne(id);
    }

    @Override
    public List<Comment> getCommentsByPostId(int id) {
        return commentJpaRepository.findAllByPostId(id);
    }

    @Override
    public void addComment(Comment comment) {
        commentJpaRepository.save(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentJpaRepository.delete(comment);
    }

    @Override
    public void deleteCommentsByPostId(int id) {
        commentJpaRepository.deleteAllByPostId(id);
    }
}
