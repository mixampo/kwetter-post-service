package rest.services;

import DTO.request.DeletePostDto;
import DTO.response.PostResponseDto;
import exceptions.NotUserPostException;
import models.Comment;
import models.Heart;
import models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.repositories.IPostJpaRepository;
import rest.services.interfaces.IPostContainerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostContainerService implements IPostContainerService {
    private final IPostJpaRepository postJpaRepository;
    private final CommentContainerService commentContainerService;
    private final HeartContainerService heartContainerService;
    private final LocationContainerService locationContainerService;

    @Autowired
    public PostContainerService(IPostJpaRepository postJpaRepository, CommentContainerService commentContainerService, HeartContainerService heartContainerService, LocationContainerService locationContainerService) {
        this.postJpaRepository = postJpaRepository;
        this.commentContainerService = commentContainerService;
        this.heartContainerService = heartContainerService;
        this.locationContainerService = locationContainerService;
    }

    @Override
    public List<Post> getUserPosts(int id) {
        return postJpaRepository.findAllByAccountId(id);
    }

    @Override
    public void addUserPost(Post post) {
        postJpaRepository.save(post);
    }

    @Override
    public List<PostResponseDto> getAllPosts() {
        List<PostResponseDto> res = new ArrayList<>();
        List<Post> p = postJpaRepository.findAll();
        for (Post x : p) {
            res.add(new PostResponseDto(x, heartContainerService.getPostHearts(x.getId()), commentContainerService.getCommentsByPostId(x.getId())));
        }
        return res;
    }

    @Override
    public void deleteUserPost(DeletePostDto deletePostDto) throws NotUserPostException {
        Optional<Post> p = postJpaRepository.findById(deletePostDto.getPostId());
        if (p.isPresent() && p.get().getAccountId() == deletePostDto.getAccountId()) {
            heartContainerService.deleteHeartsByPostId(deletePostDto.getPostId());
            commentContainerService.deleteCommentsByPostId(deletePostDto.getPostId());
            locationContainerService.deleteLocationByPostId(deletePostDto.getPostId());
            postJpaRepository.deleteById(deletePostDto.getPostId());
        } else {
            throw new NotUserPostException();
        }
    }
}
