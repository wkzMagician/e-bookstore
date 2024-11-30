package wkz.org.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "`cart`")
public class CartItem implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @Column(name = "book_amount")
    private Long bookAmount;

    public CartItem() {
    }

    public CartItem(Long userId, Long bookId, Long bookAmount) {
        this.userId = userId;
        this.bookAmount = bookAmount;
        this.book = new Book();
        this.book.setId(bookId); // 仅设置 bookId
    }
}
