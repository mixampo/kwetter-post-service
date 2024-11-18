package rest.services.interfaces;

import DTO.request.DeletePostDto;
import DTO.response.PostResponseDto;
import exceptions.NotUserPostException;
import models.Post;

import java.util.List;

public interface IPostContainerService {
    void addUserPost(Post post);
    List<PostResponseDto> getAllPosts();
    List<Post> getUserPosts(int userId);
    void deleteUserPost(DeletePostDto deletePostDto) throws NotUserPostException;
}
