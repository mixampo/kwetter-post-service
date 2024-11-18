package KwetterPostServiceIntegrationTests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Heart;
import models.Post;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
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
public class HeartControllerTests {
    private ObjectMapper mapper;
    private HttpHeaders headers;

    private String url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void init() {
        this.url = format("http://localhost:%d/heart", port);

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testGetPostHearts() throws JsonProcessingException {
        int postId = 1;
        this.url = format("http://localhost:%d/heart/post/%d", port, postId);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        List<Heart> postHearts = mapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length() > 0);
        for (Heart h : postHearts) {
            Assertions.assertEquals(postId, h.getPost().getId());
        }
    }

    @Test
    public void testAddPostHeart() {
        Heart heartRequest = new Heart(1, new Post(1));

        HttpEntity<Heart> request = new HttpEntity<>(heartRequest, headers);

        ResponseEntity<Heart> response = restTemplate.exchange(url, HttpMethod.POST, request, Heart.class);
        Heart responseHeart = response.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(responseHeart);
        Assertions.assertEquals(1, responseHeart.getPost().getId());
        Assertions.assertEquals(1, responseHeart.getAccountId());
    }

    @Test
    public void testDeletePostHeart() {
        Heart heart = new Heart(1,1, new Post(1));

        HttpEntity<Heart> request = new HttpEntity<>(heart, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
