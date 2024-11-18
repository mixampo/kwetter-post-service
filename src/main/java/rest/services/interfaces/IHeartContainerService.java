package rest.services.interfaces;

import models.Heart;

import java.util.List;

public interface IHeartContainerService {
    List<Heart> getPostHearts(int id);
    void addHeart(Heart heart);
    void deleteHeart(Heart heart);
    void deleteHeartsByPostId(int id);
}
