package rest.repositories;


import models.Heart;
import models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IHeartJpaRepository extends JpaRepository<Heart, Integer> {
    List<Heart> findAllByPostId(int id);
    void deleteAllByPostId(int id);
}
