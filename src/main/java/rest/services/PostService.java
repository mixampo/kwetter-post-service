package rest.services;

import exceptions.PostDoesNotExistException;
import models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.repositories.IPostJpaRepository;
import rest.services.interfaces.IPostService;

import java.util.Optional;

@Service
public class PostService implements IPostService {

    private final IPostJpaRepository postJpaRepository;

    @Autowired
    public PostService(IPostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public void updateUserPost(Post post) throws PostDoesNotExistException {
        Optional<Post> p = postJpaRepository.findById(post.getId());

        if (p.isPresent()) {
            postJpaRepository.save(post);
        } else {
            throw new PostDoesNotExistException();
        }
    }
}
