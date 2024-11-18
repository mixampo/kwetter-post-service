package rest.repositories;

import models.Comment;
import models.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentJpaRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByPostId(int id);
    void deleteAllByPostId(int id);
}
