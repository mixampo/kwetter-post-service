package rest.services;

import models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.repositories.ILocationJpaRepository;
import rest.services.interfaces.ILocationContainerService;

import java.util.List;

@Service
public class LocationContainerService implements ILocationContainerService {
    private final ILocationJpaRepository locationJpaRepository;

    @Autowired
    public LocationContainerService(ILocationJpaRepository locationJpaRepository) {
        this.locationJpaRepository = locationJpaRepository;
    }

    @Override
    public List<Location> getLocationByPostId(int id) {
        return locationJpaRepository.getLocationByPostId(id);
    }

    @Override
    public void addLocation(Location location) {
        locationJpaRepository.save(location);
    }

    @Override
    public void deleteLocationByPostId(int id) {
        locationJpaRepository.deleteAllByPostId(id);
    }
}
