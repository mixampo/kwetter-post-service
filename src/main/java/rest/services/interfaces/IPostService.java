package rest.services.interfaces;

import exceptions.PostDoesNotExistException;
import models.Post;

public interface IPostService {
    void updateUserPost(Post post) throws PostDoesNotExistException;
}
