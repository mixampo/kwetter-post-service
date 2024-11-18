package models;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@ToString
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int accountId;

    @ManyToOne(optional = false)
    @JoinColumn
    private Post post;

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

    public Heart(int id, int accountId, Post post) {
        this.id = id;
        this.accountId = accountId;
        this.post = post;
    }

    public Heart(int accountId, Post post) {
        this.accountId = accountId;
        this.post = post;
    }
}
