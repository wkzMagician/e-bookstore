package wkz.org.backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Entity
@Data
@Table(name = "`book`")
public class Book implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // cover url
    @Column(name = "cover")
    private String cover;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "price")
    private Integer price;

    @Column(name = "inventory")
    private Integer inventory;

    @Column(name = "ISBN")
    private String ISBN;

    // neo4j

    @Column(name = "category")
    private String category;

    // mongodb
//    @Column(name = "description")
    @Transient
    private String description;



    public Book(Long id) {
        this.id = id;
    }

    public Book() {
    }
}
