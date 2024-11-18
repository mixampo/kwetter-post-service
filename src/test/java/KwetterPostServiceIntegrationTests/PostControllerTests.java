package KwetterPostServiceIntegrationTests;

import DTO.request.DeletePostDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import javax.net.ssl.SSLContext;
import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(
        classes = KwetterPostServiceRestServer.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostControllerTests {
    private ObjectMapper mapper;
    private HttpHeaders headers;

    private String url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void init() {
        this.url = format("http://localhost:%d/post", port);

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testGetAllPosts() {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length() > 0);
    }

    @Test
    public void testGetUserPosts() throws JsonProcessingException {
        int accountId = 1;
        this.url = format("http://localhost:%d/post/account/%d", port, accountId);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        List<Post> posts = mapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length() > 0);
        for (Post p : posts) {
            Assertions.assertEquals(accountId, p.getAccountId());
        }
    }

    @Test
    public void testCreateUserPost() {
        Post requestPost = new Post(1, "This is a test post created by a Spring Integration test", LocalDate.parse("2021-11-11"));

        HttpEntity<Post> request = new HttpEntity<>(requestPost, headers);

        ResponseEntity<Post> response = restTemplate.exchange(url, HttpMethod.POST, request, Post.class);
        Post responsePost = response.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(responsePost);
        Assertions.assertEquals(1, responsePost.getAccountId());
        Assertions.assertEquals("This is a test post created by a Spring Integration test", responsePost.getContent());
        Assertions.assertEquals(LocalDate.parse("2021-11-11"), responsePost.getPostDate());
    }

    @Test
    public void testUpdateUserPost() {
        Post requestUpdatePost = new Post(1, 1, "This existing post has been updated by a Spring Integration test", LocalDate.parse("2021-12-12"));

        HttpEntity<Post> request = new HttpEntity<>(requestUpdatePost, headers);

        ResponseEntity<Post> response = restTemplate.exchange(url, HttpMethod.PUT, request, Post.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateNonExistingUserPost() {
        Post requestUpdatePost = new Post(800, 1, "This post does not exist so the the API will return a BAD_REQUEST response", LocalDate.parse("2021-12-12"));

        HttpEntity<Post> request = new HttpEntity<>(requestUpdatePost, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        String message = response.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("This post does not exist!", message);
    }

    @Test
    public void testDeleteUserPost() {
        this.url = format("http://localhost:%d/post", port);
        DeletePostDto deletePostDto = new DeletePostDto(3, 1);

        HttpEntity<DeletePostDto> request = new HttpEntity<>(deletePostDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteNotUserPost() {
        DeletePostDto deletePostDto = new DeletePostDto(1, 2);

        HttpEntity<DeletePostDto> request = new HttpEntity<>(deletePostDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        String message = response.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Can't update this post!", message);
    }
}
