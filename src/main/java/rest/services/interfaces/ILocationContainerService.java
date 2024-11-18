package rest.services.interfaces;

import models.Location;

import java.util.List;

public interface ILocationContainerService {
    List<Location> getLocationByPostId(int id);
    void addLocation(Location location);
    void deleteLocationByPostId(int id);
}
