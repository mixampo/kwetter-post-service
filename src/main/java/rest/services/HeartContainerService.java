package rest.services;

import models.Heart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.repositories.IHeartJpaRepository;
import rest.services.interfaces.IHeartContainerService;

import java.util.List;

@Service
public class HeartContainerService implements IHeartContainerService {
    private final IHeartJpaRepository heartJpaRepository;

    @Autowired
    public HeartContainerService(IHeartJpaRepository heartJpaRepository) {
        this.heartJpaRepository = heartJpaRepository;
    }


    @Override
    public List<Heart> getPostHearts(int id) {
        return heartJpaRepository.findAllByPostId(id);
    }

    @Override
    public void addHeart(Heart heart) {
        heartJpaRepository.save(heart);
    }

    @Override
    public void deleteHeart(Heart heart) {
        heartJpaRepository.delete(heart);
    }

    @Override
    public void deleteHeartsByPostId(int id) {
        heartJpaRepository.deleteAllByPostId(id);
    }
}
