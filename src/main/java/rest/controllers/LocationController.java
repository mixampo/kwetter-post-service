package rest.controllers;

import models.Comment;
import models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rest.services.LocationContainerService;

import java.util.List;

@RestController
public class LocationController {
    private final LocationContainerService locationContainerService;

    @Autowired
    public LocationController(LocationContainerService locationContainerService) {
        this.locationContainerService = locationContainerService;
    }

    @GetMapping(value = "/location/post/{id}")
    public List<Location> getPostLocation(@PathVariable("id") int id) {
        return locationContainerService.getLocationByPostId(id);
    }

    @PostMapping(value = "/location", headers = "Accept=application/json")
    public ResponseEntity<?> addLocation(@RequestBody Location location, UriComponentsBuilder ucBuilder) {
        HttpHeaders headers = new HttpHeaders();
        try {
            locationContainerService.addLocation(location);
            headers.setLocation(ucBuilder.path("/location/{id}").buildAndExpand(location.getId()).toUri());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(location, headers, HttpStatus.CREATED);
    }
}
