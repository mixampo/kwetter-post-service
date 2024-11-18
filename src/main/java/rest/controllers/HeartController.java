package rest.controllers;

import models.Heart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rest.services.HeartContainerService;

import java.util.List;

@RestController
public class HeartController {
    private final HeartContainerService heartContainerService;

    @Autowired
    public HeartController(HeartContainerService heartContainerService) {
        this.heartContainerService = heartContainerService;
    }

    @GetMapping(value = "/heart/post/{id}")
    public List<Heart> getPostHearts(@PathVariable("id") int id) {
        return heartContainerService.getPostHearts(id);
    }

    @PostMapping(value = "/heart", headers = "Accept=application/json")
    public ResponseEntity<?> createHeart(@RequestBody Heart heart, UriComponentsBuilder ucBuilder) {
        HttpHeaders headers = new HttpHeaders();
        try {
            heartContainerService.addHeart(heart);
            headers.setLocation(ucBuilder.path("/heart/{id}").buildAndExpand(heart.getId()).toUri());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(heart, headers, HttpStatus.CREATED);
    }

    @Transactional
    @DeleteMapping(value = "/heart")
    public ResponseEntity<?> deletePostHeart(@RequestBody Heart heart) {
        try {
            heartContainerService.deleteHeart(heart);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
