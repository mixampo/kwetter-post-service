package models;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(optional = false)
    @JoinColumn
    private Post post;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Location(int id, Post post, String longitude, String latitude) {
        this.id = id;
        this.post = post;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(Post post, String longitude, String latitude) {
        this.post = post;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
