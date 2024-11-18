package KwetterPostServiceIntegrationTests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Location;
import models.Post;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import rest.KwetterPostServiceRestServer;

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
public class LocationControllerTests {
    private ObjectMapper mapper;

    private String url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void init() {
        this.url = format("http://localhost:%d/location", port);

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testGetPostLocation() throws JsonProcessingException {
        int postId = 1;
        this.url = format("http://localhost:%d/location/post/%d", port, postId);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        List<Location> locations = mapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length() > 0);
        for (Location l : locations) {
            Assertions.assertEquals(postId, l.getPost().getId());
        }
    }

    @Test
    public void testAddPostLocation() {
        this.url = format("http://localhost:%d/location", port);
        Location locationRequest = new Location(new Post(2), "1289764,6534", "3078563,8960");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Location> request = new HttpEntity<>(locationRequest, headers);

        ResponseEntity<Location> response = restTemplate.exchange(url, HttpMethod.POST, request, Location.class);
        Location responseLocation = response.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(responseLocation);
        Assertions.assertEquals(2, responseLocation.getPost().getId());
        Assertions.assertEquals("1289764,6534", responseLocation.getLongitude());
        Assertions.assertEquals("3078563,8960", responseLocation.getLatitude());
    }

    @Test
    public void testAddSecondPostLocation() {
        this.url = format("http://localhost:%d/location", port);
        Location locationRequest = new Location(new Post(1, 1, "Test post", LocalDate.parse("2021-04-23")), "1289764,6534", "3078563,8960");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Location> request = new HttpEntity<>(locationRequest, headers);

        ResponseEntity<Location> response = restTemplate.exchange(url, HttpMethod.POST, request, Location.class);
        Location responseLocation = response.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(responseLocation);
    }
}
