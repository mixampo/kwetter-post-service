package models;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int accountId;

    @Column(nullable = false)
    private String Content;

    @Column(nullable = false)
    private LocalDate postDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Post(int id, int accountId, String content, LocalDate postDate) {
        this.id = id;
        this.accountId = accountId;
        Content = content;
        this.postDate = postDate;
    }

    public Post(int accountId, String content, LocalDate postDate) {
        this.accountId = accountId;
        Content = content;
        this.postDate = postDate;
    }

    public Post(int id) {
        this.id = id;
    }
}
