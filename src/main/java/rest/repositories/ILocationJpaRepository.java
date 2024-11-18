package rest.repositories;

import models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILocationJpaRepository extends JpaRepository<Location, Integer> {
    List<Location> getLocationByPostId(int id);
    void deleteAllByPostId(int id);
}
