package KwetterPostServiceIntegrationTests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Comment;
import models.Post;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import rest.KwetterPostServiceRestServer;
import java.util.List;

import static java.lang.String.format;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(
        classes = KwetterPostServiceRestServer.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentControllerTests {
    private ObjectMapper mapper;

    private String url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void init() {
        this.url = format("http://localhost:%d/comment", port);

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    @Test
    public void testGetPostComments() throws JsonProcessingException {
        int postId = 1;
        this.url = format("http://localhost:%d/comment/post/%d", port, postId);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        List<Comment> postComments = mapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length() > 0);

        for (Comment c : postComments) {
            Assertions.assertEquals(postId, c.getPost().getId());
            Assertions.assertNotNull(c.getContent());
        }
    }

    @Test
    public void testCreateComment() {
        this.url = format("http://localhost:%d/comment", port);
        Comment commentRequest = new Comment(1, new Post(1), "This is a test comment posted by a Spring Integration test");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Comment> request = new HttpEntity<>(commentRequest, headers);

        ResponseEntity<Comment> response = restTemplate.exchange(url, HttpMethod.POST, request, Comment.class);
        Comment responseComment = response.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(responseComment);
        Assertions.assertEquals(1, responseComment.getPost().getId());
        Assertions.assertEquals(1, responseComment.getAccountId());
    }

    @Test
    public void testDeleteUserComment() {
        Comment comment = new Comment(1, 1, new Post(1), "Test comment for test post which was created on th 23th of May 2021");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Comment> request = new HttpEntity<>(comment, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
