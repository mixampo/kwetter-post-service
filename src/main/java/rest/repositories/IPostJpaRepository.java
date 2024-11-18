package rest.repositories;

import models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPostJpaRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByAccountId(int id);
}
