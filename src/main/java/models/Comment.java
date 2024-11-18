package models;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int accountId;

    @ManyToOne(optional = false)
    @JoinColumn
    private Post post;

    @Column(nullable = false)
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment(int id, int accountId, Post post, String content) {
        this.id = id;
        this.accountId = accountId;
        this.post = post;
        this.content = content;
    }

    public Comment(int accountId, Post post, String content) {
        this.accountId = accountId;
        this.post = post;
        this.content = content;
    }
}
