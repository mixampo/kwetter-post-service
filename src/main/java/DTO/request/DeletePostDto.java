package DTO.request;

import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class DeletePostDto {
    private int postId;
    private int accountId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public DeletePostDto(int postId, int accountId) {
        this.postId = postId;
        this.accountId = accountId;
    }
}
