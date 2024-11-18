package DTO.response;

import lombok.NoArgsConstructor;
import lombok.ToString;
import models.Comment;
import models.Heart;
import models.Post;

import java.util.List;

@NoArgsConstructor
@ToString
public class PostResponseDto {
    private Post post;
    private List<Heart> hearts;
    private List<Comment> comments;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Heart> getHearts() {
        return hearts;
    }

    public void setHearts(List<Heart> hearts) {
        this.hearts = hearts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public PostResponseDto(Post post, List<Heart> hearts, List<Comment> comments) {
        this.post = post;
        this.hearts = hearts;
        this.comments = comments;
    }
}
